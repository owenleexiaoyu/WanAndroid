package cc.lixiaoyu.wanandroid.core.home

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
import cc.lixiaoyu.wanandroid.kmp.home.Article
import cc.lixiaoyu.wanandroid.kmp.home.HomeEffect
import cc.lixiaoyu.wanandroid.kmp.home.HomeIntent
import cc.lixiaoyu.wanandroid.kmp.home.HomeScreen
import cc.lixiaoyu.wanandroid.kmp.theme.ThemeControllerRegistry
import cc.lixiaoyu.wanandroid.kmp.theme.WanTheme
import cc.lixiaoyu.wanandroid.util.ToastUtil

interface HomeNavigator {
    fun openHomeSearch()
    fun openHomeArticle(article: Article)
    fun openHomeLogin()
}

class HomeFragment : Fragment() {
    private val viewModel by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }

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
                    HomeScreen(viewModel.shared) { effect ->
                        when (effect) {
                            HomeEffect.OpenSearch -> navigator.openHomeSearch()
                            HomeEffect.RequestLogin -> navigator.openHomeLogin()
                            is HomeEffect.OpenArticle -> navigator.openHomeArticle(effect.article)
                            is HomeEffect.Message -> ToastUtil.showToast(effect.text)
                        }
                    }
                }
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AccountManager.isLoginLiveData.observe(viewLifecycleOwner) {
            viewModel.shared.dispatch(HomeIntent.SessionChanged(it))
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.shared.dispatch(HomeIntent.SessionChanged(AccountManager.isLogin()))
    }
    companion object { fun newInstance() = HomeFragment() }
}
