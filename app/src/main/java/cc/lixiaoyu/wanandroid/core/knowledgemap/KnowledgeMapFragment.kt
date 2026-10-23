package cc.lixiaoyu.wanandroid.core.knowledgemap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.TypedValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cc.lixiaoyu.wanandroid.core.account.AccountManager
import cc.lixiaoyu.wanandroid.core.home.HomeNavigator
import cc.lixiaoyu.wanandroid.kmp.knowledge.KnowledgeEffect
import cc.lixiaoyu.wanandroid.kmp.knowledge.KnowledgeIntent
import cc.lixiaoyu.wanandroid.kmp.knowledge.KnowledgeScreen
import cc.lixiaoyu.wanandroid.kmp.theme.ThemeControllerRegistry
import cc.lixiaoyu.wanandroid.kmp.theme.WanTheme
import cc.lixiaoyu.wanandroid.util.ToastUtil

class KnowledgeMapFragment : Fragment() {
    private val viewModel by lazy { ViewModelProvider(this)[KnowledgeMapViewModel::class.java] }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val navigator = requireActivity() as HomeNavigator
        return ComposeView(requireContext()).apply {
            val bottomBarSize = TypedValue()
            requireContext().theme.resolveAttribute(androidx.appcompat.R.attr.actionBarSize, bottomBarSize, true)
            setPadding(0, 0, 0, TypedValue.complexToDimensionPixelSize(bottomBarSize.data, resources.displayMetrics))
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val dark by ThemeControllerRegistry.controller.isDarkMode.collectAsState()
                WanTheme(darkTheme = dark) {
                    KnowledgeScreen(viewModel.shared) { effect ->
                        when (effect) {
                            KnowledgeEffect.RequestLogin -> navigator.openHomeLogin()
                            is KnowledgeEffect.OpenArticle -> navigator.openHomeArticle(effect.article)
                            is KnowledgeEffect.Message -> ToastUtil.showToast(effect.text)
                        }
                    }
                }
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AccountManager.isLoginLiveData.observe(viewLifecycleOwner) {
            viewModel.shared.dispatch(KnowledgeIntent.SessionChanged(it))
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.shared.dispatch(KnowledgeIntent.SessionChanged(AccountManager.isLogin()))
    }
    companion object { fun newInstance() = KnowledgeMapFragment() }
}
