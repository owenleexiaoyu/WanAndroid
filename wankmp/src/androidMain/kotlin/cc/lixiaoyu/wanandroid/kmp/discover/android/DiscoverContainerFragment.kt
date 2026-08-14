package cc.lixiaoyu.wanandroid.kmp.discover.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import cc.lixiaoyu.wanandroid.kmp.AndroidToastContext
import cc.lixiaoyu.wanandroid.kmp.discover.data.remote.DiscoverRemoteDataSource
import cc.lixiaoyu.wanandroid.kmp.discover.domain.DiscoverRepository
import cc.lixiaoyu.wanandroid.kmp.discover.mvi.DiscoverStore
import cc.lixiaoyu.wanandroid.kmp.discover.ui.DiscoverScreen
import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.createNavigationHttpClient
import cc.lixiaoyu.wanandroid.kmp.showShortToast
import cc.lixiaoyu.wanandroid.kmp.theme.ThemeControllerRegistry
import cc.lixiaoyu.wanandroid.kmp.theme.WanTheme
import io.ktor.client.HttpClient

class DiscoverContainerFragment : Fragment() {

    private var httpClient: HttpClient? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        AndroidToastContext.init(requireContext().applicationContext)

        val opener = requireActivity() as? DiscoverWebDetailOpener
            ?: error("${requireActivity().javaClass.simpleName} must implement ${DiscoverWebDetailOpener::class.java.name}")

        val client = createNavigationHttpClient().also { httpClient = it }
        val repository = DiscoverRepository(DiscoverRemoteDataSource(client))
        val store = DiscoverStore(repository, lifecycleScope)
        val themeController = ThemeControllerRegistry.controller

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val isDarkMode by themeController.isDarkMode.collectAsState()
                WanTheme(darkTheme = isDarkMode) {
                    DiscoverScreen(
                        store = store,
                        onOpenUrl = { effect ->
                            opener.openDiscoverWebDetail(
                                this@DiscoverContainerFragment.requireContext(),
                                effect.title,
                                effect.url,
                            )
                        },
                        onShowToast = { message ->
                            showShortToast(message)
                        },
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        httpClient?.close()
        httpClient = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): DiscoverContainerFragment = DiscoverContainerFragment()
    }
}
