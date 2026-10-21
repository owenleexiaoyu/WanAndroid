package cc.lixiaoyu.wanandroid.kmp

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import cc.lixiaoyu.wanandroid.kmp.mine.ui.MineAction
import cc.lixiaoyu.wanandroid.kmp.mine.ui.MineScreen
import cc.lixiaoyu.wanandroid.kmp.mine.ui.MineUser
import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.NavRemoteDataSource
import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.createNavigationHttpClient
import cc.lixiaoyu.wanandroid.kmp.nav.domain.NavRepository
import cc.lixiaoyu.wanandroid.kmp.nav.mvi.NavStore
import cc.lixiaoyu.wanandroid.kmp.nav.ui.NavScreen
import cc.lixiaoyu.wanandroid.kmp.theme.WanTheme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

fun MainViewController() = ComposeUIViewController { App() }

/**
 * iOS 宿主容器：承载 KMP [NavScreen]。
 *
 * Swift/SwiftUI 侧通过 `NavViewControllerKt.NavViewController()` 调用。
 */
fun NavViewController() = ComposeUIViewController {
    val scope = MainScope()
    val httpClient = createNavigationHttpClient()
    val repository = NavRepository(NavRemoteDataSource(httpClient))
    val store = NavStore(repository, scope)

    DisposableEffect(Unit) {
        onDispose {
            httpClient.close()
            scope.cancel()
        }
    }

    WanTheme(darkTheme = false) {
        NavScreen(
            store = store,
            showTopBar = true,
            onOpenUrl = { _ ->
                // iOS 侧可在后续版本通过 effect 回调接入 WebView/浏览器
            },
        )
    }
}

/**
 * iOS 宿主容器：承载 KMP [MineScreen]。
 *
 * Swift/SwiftUI 侧通过 `MineViewControllerKt.MineViewController()` 调用。
 */
fun MineViewController() = ComposeUIViewController {
    WanTheme(darkTheme = false) {
        MineScreen(
            user = MineUser(name = "小小的太太阳", id = "27165"),
            showTopBar = true,
            onAction = { _: MineAction ->
                // iOS 侧可在后续版本接入登录/跳转等动作
            },
        )
    }
}

/** Standalone shared home; native navigation is supplied by the iOS host. */
fun HomeViewController(
    onSearch: () -> Unit,
    onArticle: (cc.lixiaoyu.wanandroid.kmp.home.Article) -> Unit,
    onLogin: () -> Unit,
) = ComposeUIViewController {
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    val client = androidx.compose.runtime.remember { createNavigationHttpClient() }
    val viewModel = androidx.compose.runtime.remember(client) {
        cc.lixiaoyu.wanandroid.kmp.home.HomeViewModel(
            cc.lixiaoyu.wanandroid.kmp.home.KtorHomeRepository(client), scope,
        )
    }
    var message by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<String?>(null) }
    DisposableEffect(viewModel) {
        onDispose { viewModel.close(); client.close() }
    }
    WanTheme(darkTheme = androidx.compose.foundation.isSystemInDarkTheme()) {
        cc.lixiaoyu.wanandroid.kmp.home.HomeScreen(viewModel) { effect ->
            when (effect) {
                cc.lixiaoyu.wanandroid.kmp.home.HomeEffect.OpenSearch -> onSearch()
                cc.lixiaoyu.wanandroid.kmp.home.HomeEffect.RequestLogin -> onLogin()
                is cc.lixiaoyu.wanandroid.kmp.home.HomeEffect.OpenArticle -> onArticle(effect.article)
                is cc.lixiaoyu.wanandroid.kmp.home.HomeEffect.Message -> message = effect.text
            }
        }
        message?.let { text ->
            androidx.compose.material.AlertDialog(
                onDismissRequest = { message = null },
                text = { androidx.compose.material.Text(text) },
                confirmButton = { androidx.compose.material.TextButton(onClick = { message = null }) { androidx.compose.material.Text("确定") } },
            )
        }
    }
}
