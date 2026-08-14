package cc.lixiaoyu.wanandroid.kmp.nav.android

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
import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.NavRemoteDataSource
import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.createNavigationHttpClient
import cc.lixiaoyu.wanandroid.kmp.nav.domain.NavRepository
import cc.lixiaoyu.wanandroid.kmp.nav.mvi.NavStore
import cc.lixiaoyu.wanandroid.kmp.nav.ui.NavScreen
import cc.lixiaoyu.wanandroid.kmp.theme.ThemeControllerRegistry
import cc.lixiaoyu.wanandroid.kmp.theme.WanTheme
import io.ktor.client.HttpClient

/**
 * 承载 KMP [NavScreen] 的导航 Tab 容器；宿主 Activity 需实现 [NavWebDetailOpener]。
 */
class NavContainerFragment : Fragment() {

    private var httpClient: HttpClient? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        AndroidToastContext.init(requireContext().applicationContext)

        val opener = requireActivity() as? NavWebDetailOpener
            ?: error("${requireActivity().javaClass.simpleName} must implement ${NavWebDetailOpener::class.java.name}")

        val client = createNavigationHttpClient().also { httpClient = it }
        val repository = NavRepository(NavRemoteDataSource(client))
        val store = NavStore(repository, lifecycleScope)
        val themeController = ThemeControllerRegistry.controller

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val isDarkMode by themeController.isDarkMode.collectAsState()
                WanTheme(darkTheme = isDarkMode) {
                    NavScreen(
                        store = store,
                        showTopBar = false,
                        onOpenUrl = { effect ->
                            opener.openNavWebDetail(
                                this@NavContainerFragment.requireContext(),
                                effect.title,
                                effect.url,
                            )
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
        fun newInstance(): NavContainerFragment = NavContainerFragment()
    }
}
