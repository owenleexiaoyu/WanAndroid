package cc.lixiaoyu.wanandroid.kmp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cc.lixiaoyu.wanandroid.kmp.mine.ui.MineAction
import cc.lixiaoyu.wanandroid.kmp.mine.ui.MineScreen
import cc.lixiaoyu.wanandroid.kmp.mine.ui.MineUser
import cc.lixiaoyu.wanandroid.kmp.discover.data.remote.DiscoverRemoteDataSource
import cc.lixiaoyu.wanandroid.kmp.discover.domain.DiscoverRepository
import cc.lixiaoyu.wanandroid.kmp.discover.mvi.DiscoverEffect
import cc.lixiaoyu.wanandroid.kmp.discover.mvi.DiscoverStore
import cc.lixiaoyu.wanandroid.kmp.discover.ui.DiscoverScreen
import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.NavRemoteDataSource
import cc.lixiaoyu.wanandroid.kmp.nav.domain.NavRepository
import cc.lixiaoyu.wanandroid.kmp.nav.mvi.NavStore
import cc.lixiaoyu.wanandroid.kmp.nav.testing.NavNetworkMock
import cc.lixiaoyu.wanandroid.kmp.nav.testing.NavNetworkMockScenario
import cc.lixiaoyu.wanandroid.kmp.nav.testing.createNavHttpClient
import cc.lixiaoyu.wanandroid.kmp.nav.ui.NavScreen
import cc.lixiaoyu.wanandroid.kmp.settings.ui.SettingsAction
import cc.lixiaoyu.wanandroid.kmp.settings.ui.SettingsScreen
import cc.lixiaoyu.wanandroid.kmp.theme.MemoryThemeController
import cc.lixiaoyu.wanandroid.kmp.theme.WanTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * 宿主入口：上方为网络 Mock 调试条，主体为正式导航页 [NavScreen]。
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview
fun App() {
    val themeController = remember { MemoryThemeController() }
    val isDarkMode by themeController.isDarkMode.collectAsState()

    WanTheme(darkTheme = isDarkMode) {
        val scope = rememberCoroutineScope()
        val mockScenario by NavNetworkMock.scenario.collectAsState()
        val client = remember(mockScenario) { createNavHttpClient(mockScenario) }
        DisposableEffect(client) {
            onDispose { client.close() }
        }

        val repository = remember(client) {
            NavRepository(NavRemoteDataSource(client))
        }

        val store = remember(repository, scope) {
            NavStore(repository, scope)
        }
        val discoverRepository = remember(client) {
            DiscoverRepository(DiscoverRemoteDataSource(client))
        }
        val discoverStore = remember(discoverRepository, scope) {
            DiscoverStore(discoverRepository, scope)
        }

        var page by remember { mutableStateOf("nav") }

        Column(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Text(
                    "网络 Mock（仅调试）",
                    style = MaterialTheme.typography.caption,
                    color = Color.Gray,
                )
                Text(
                    mockScenarioLabel(mockScenario),
                    style = MaterialTheme.typography.caption,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 6.dp),
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(onClick = { NavNetworkMock.setScenario(NavNetworkMockScenario.None) }) {
                        Text("关")
                    }
                    Button(onClick = {
                        NavNetworkMock.setScenario(NavNetworkMockScenario.WeakNetworkSuccess(delayMillis = 3_000L))
                    }) { Text("弱网→成") }
                    Button(onClick = {
                        NavNetworkMock.setScenario(NavNetworkMockScenario.WeakNetworkFailure(delayMillis = 3_000L))
                    }) { Text("弱网→败") }
                    Button(onClick = {
                        NavNetworkMock.setScenario(NavNetworkMockScenario.ConnectionFailure())
                    }) { Text("断连") }
                    Button(onClick = {
                        NavNetworkMock.setScenario(
                            NavNetworkMockScenario.ApiBusinessFailure(errorCode = -1, errorMsg = "mock api error"),
                        )
                    }) { Text("API败") }
                }
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Button(onClick = { page = "nav" }) { Text("导航") }
                Button(onClick = { page = "discover" }) { Text("发现") }
                Button(onClick = { page = "mine" }) { Text("我的") }
                Button(onClick = { page = "settings" }) { Text("设置") }
            }

            when (page) {
                "discover" -> {
                    DiscoverScreen(
                        store = discoverStore,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        onOpenUrl = { effect: DiscoverEffect.OpenUrl ->
                            showShortToast("${effect.title}\n${effect.url}")
                        },
                        onShowToast = { message ->
                            showShortToast(message)
                        },
                    )
                }
                "settings" -> {
                    SettingsScreen(
                        themeController = themeController,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        onBack = { page = "mine" },
                        onItemClick = { action ->
                            val label = when (action) {
                                SettingsAction.FontSize -> "点击：字体大小"
                                SettingsAction.ClearCache -> "点击：清除缓存"
                                SettingsAction.CheckVersion -> "点击：检查版本"
                                SettingsAction.AboutUs -> "点击：关于我们"
                                SettingsAction.Logout -> "点击：退出登录"
                            }
                            showShortToast(label)
                        },
                    )
                }
                "mine" -> {
                    MineScreen(
                        user = MineUser(name = "小小的太太阳", id = "27165"),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        onAction = { action ->
                            if (action == MineAction.ClickSystemSettings) {
                                page = "settings"
                                return@MineScreen
                            }

                            val label = when (action) {
                                MineAction.ClickProfile -> "点击：个人信息"
                                MineAction.ClickMyPoints -> "点击：我的积分"
                                MineAction.ClickPointsRank -> "点击：积分排行"
                                MineAction.ClickMyShare -> "点击：我的分享"
                                MineAction.ClickMyCollect -> "点击：我的收藏"
                                MineAction.ClickBrowseHistory -> "点击：浏览历史"
                                MineAction.ClickOpenSourceLicense -> "点击：开源许可"
                                MineAction.ClickAboutAuthor -> "点击：关于作者"
                                MineAction.ClickSystemSettings -> error("handled above")
                            }
                            showShortToast(label)
                        },
                    )
                }
                else -> {
                    NavScreen(
                        store = store,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        onOpenUrl = { effect ->
                            showShortToast("${effect.title}\n${effect.url}")
                        },
                    )
                }
            }
        }
    }
}

private fun mockScenarioLabel(s: NavNetworkMockScenario): String = when (s) {
    NavNetworkMockScenario.None -> "关闭"
    is NavNetworkMockScenario.WeakNetworkSuccess -> "弱网→成功(+${s.delayMillis}ms)"
    is NavNetworkMockScenario.WeakNetworkFailure -> "弱网→失败(+${s.delayMillis}ms)"
    is NavNetworkMockScenario.ConnectionFailure -> "连接失败"
    is NavNetworkMockScenario.ApiBusinessFailure -> "API失败(code=${s.errorCode})"
}
