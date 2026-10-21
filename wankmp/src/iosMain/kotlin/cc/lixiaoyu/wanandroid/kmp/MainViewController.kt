package cc.lixiaoyu.wanandroid.kmp

import androidx.compose.runtime.*
import androidx.compose.ui.window.ComposeUIViewController
import cc.lixiaoyu.wanandroid.kmp.discover.data.remote.DiscoverRemoteDataSource
import cc.lixiaoyu.wanandroid.kmp.discover.domain.DiscoverRepository
import cc.lixiaoyu.wanandroid.kmp.discover.mvi.DiscoverStore
import cc.lixiaoyu.wanandroid.kmp.discover.ui.DiscoverScreen
import cc.lixiaoyu.wanandroid.kmp.mine.ui.MineAction
import cc.lixiaoyu.wanandroid.kmp.mine.ui.MineScreen
import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.NavRemoteDataSource
import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.createNavigationHttpClient
import cc.lixiaoyu.wanandroid.kmp.nav.domain.NavRepository
import cc.lixiaoyu.wanandroid.kmp.nav.mvi.NavStore
import cc.lixiaoyu.wanandroid.kmp.nav.ui.NavScreen
import cc.lixiaoyu.wanandroid.kmp.settings.ui.SettingsAction
import cc.lixiaoyu.wanandroid.kmp.settings.ui.SettingsScreen
import cc.lixiaoyu.wanandroid.kmp.theme.MemoryThemeController
import cc.lixiaoyu.wanandroid.kmp.theme.WanTheme
import platform.Foundation.NSUserDefaults

private val iosThemeController = MemoryThemeController(
    initialDarkMode = NSUserDefaults.standardUserDefaults.boolForKey("wan_dark_mode"),
)

@Composable
private fun IosWanTheme(content: @Composable () -> Unit) {
    val darkMode by iosThemeController.isDarkMode.collectAsState()
    LaunchedEffect(darkMode) {
        NSUserDefaults.standardUserDefaults.setBool(darkMode, forKey = "wan_dark_mode")
    }
    WanTheme(darkTheme = darkMode, content = content)
}

fun MainViewController() = ComposeUIViewController { App() }

fun NavViewController(onOpenUrl: (String) -> Unit) = ComposeUIViewController {
    val scope = rememberCoroutineScope()
    val client = remember { createNavigationHttpClient() }
    val store = remember(client) { NavStore(NavRepository(NavRemoteDataSource(client)), scope) }
    DisposableEffect(client) { onDispose { client.close() } }
    IosWanTheme {
        NavScreen(store = store, showTopBar = true, onOpenUrl = { onOpenUrl(it.url) })
    }
}

fun DiscoverViewController(
    onOpenUrl: (String) -> Unit,
    onMessage: (String) -> Unit,
) = ComposeUIViewController {
    val scope = rememberCoroutineScope()
    val client = remember { createNavigationHttpClient() }
    val store = remember(client) { DiscoverStore(DiscoverRepository(DiscoverRemoteDataSource(client)), scope) }
    DisposableEffect(client) { onDispose { client.close() } }
    IosWanTheme {
        DiscoverScreen(store = store, onOpenUrl = { onOpenUrl(it.url) }, onShowToast = onMessage)
    }
}

fun MineViewController(onSettings: () -> Unit, onMessage: (String) -> Unit) = ComposeUIViewController {
    IosWanTheme {
        MineScreen(user = null, showTopBar = true, onAction = { action ->
            when (action) {
                MineAction.ClickSystemSettings -> onSettings()
                MineAction.ClickProfile -> onMessage("iOS 登录页尚未接入")
                MineAction.ClickMyPoints -> onMessage("iOS 我的积分页尚未接入")
                MineAction.ClickPointsRank -> onMessage("iOS 积分排行页尚未接入")
                MineAction.ClickMyShare -> onMessage("iOS 我的分享页尚未接入")
                MineAction.ClickMyCollect -> onMessage("iOS 我的收藏页尚未接入")
                MineAction.ClickBrowseHistory -> onMessage("iOS 浏览历史页尚未接入")
                MineAction.ClickOpenSourceLicense -> onMessage("iOS 开源许可页尚未接入")
                MineAction.ClickAboutAuthor -> onMessage("iOS 关于作者页尚未接入")
            }
        })
    }
}

fun SettingsViewController(onBack: () -> Unit, onMessage: (String) -> Unit) = ComposeUIViewController {
    IosWanTheme {
        SettingsScreen(themeController = iosThemeController, onBack = onBack, onItemClick = { action ->
            onMessage(when (action) {
                SettingsAction.FontSize -> "iOS 字体大小设置尚未接入"
                SettingsAction.ClearCache -> "iOS 缓存清理尚未接入"
                SettingsAction.CheckVersion -> "iOS 版本检查尚未接入"
                SettingsAction.AboutUs -> "iOS 关于我们页尚未接入"
                SettingsAction.Logout -> "当前未登录"
            })
        })
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
    IosWanTheme {
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
