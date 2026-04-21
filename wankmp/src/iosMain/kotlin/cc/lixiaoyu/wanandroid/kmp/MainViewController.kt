package cc.lixiaoyu.wanandroid.kmp

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.window.ComposeUIViewController
import cc.lixiaoyu.wanandroid.kmp.mine.ui.MineAction
import cc.lixiaoyu.wanandroid.kmp.mine.ui.MineScreen
import cc.lixiaoyu.wanandroid.kmp.mine.ui.MineUser
import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.NavRemoteDataSource
import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.createNavigationHttpClient
import cc.lixiaoyu.wanandroid.kmp.nav.domain.NavRepository
import cc.lixiaoyu.wanandroid.kmp.nav.mvi.NavStore
import cc.lixiaoyu.wanandroid.kmp.nav.ui.NavScreen
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

    MaterialTheme {
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
    MaterialTheme {
        MineScreen(
            user = MineUser(name = "小小的太太阳", id = "27165"),
            showTopBar = true,
            onAction = { _: MineAction ->
                // iOS 侧可在后续版本接入登录/跳转等动作
            },
        )
    }
}