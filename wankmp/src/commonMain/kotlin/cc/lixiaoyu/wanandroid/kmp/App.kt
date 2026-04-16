package cc.lixiaoyu.wanandroid.kmp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cc.lixiaoyu.wanandroid.kmp.nav.data.dto.Nav
import cc.lixiaoyu.wanandroid.kmp.nav.data.dto.NavItem
import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.NavRemoteDataSource
import cc.lixiaoyu.wanandroid.kmp.nav.domain.NavRepository
import cc.lixiaoyu.wanandroid.kmp.nav.mvi.NavEffect
import cc.lixiaoyu.wanandroid.kmp.nav.mvi.NavIntent
import cc.lixiaoyu.wanandroid.kmp.nav.mvi.NavStore
import cc.lixiaoyu.wanandroid.kmp.nav.testing.NavNetworkMock
import cc.lixiaoyu.wanandroid.kmp.nav.testing.NavNetworkMockScenario
import cc.lixiaoyu.wanandroid.kmp.nav.testing.createNavHttpClient
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * 临时：接入 Nav MVI（[NavStore]），[FlowRow] + [Button] 展示条目，点击通过 [showShortToast] 提示。
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
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

        LaunchedEffect(store) {
            coroutineScope {
                launch {
                    store.effect.collect { effect ->
                        if (effect is NavEffect.OpenUrl) {
                            showShortToast("${effect.title}\n${effect.url}")
                        }
                    }
                }
                store.dispatch(NavIntent.Enter)
            }
        }

        val uiState by store.state.collectAsState()

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            item {
                Text(
                    "Nav MVI（临时测试）",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "GET ${NavRemoteDataSource.NAV_JSON_URL}",
                    style = MaterialTheme.typography.caption,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                Text(
                    "网络 Mock：${mockScenarioLabel(mockScenario)}",
                    style = MaterialTheme.typography.caption,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 6.dp),
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                ) {
                    Button(onClick = { NavNetworkMock.setScenario(NavNetworkMockScenario.None) }) {
                        Text("Mock:关")
                    }
                    Button(onClick = {
                        NavNetworkMock.setScenario(NavNetworkMockScenario.WeakNetworkSuccess(delayMillis = 3_000L))
                    }) { Text("弱网3s→成功") }
                    Button(onClick = {
                        NavNetworkMock.setScenario(NavNetworkMockScenario.WeakNetworkFailure(delayMillis = 3_000L))
                    }) { Text("弱网3s→失败") }
                    Button(onClick = {
                        NavNetworkMock.setScenario(NavNetworkMockScenario.ConnectionFailure())
                    }) { Text("连接失败") }
                    Button(onClick = {
                        NavNetworkMock.setScenario(
                            NavNetworkMockScenario.ApiBusinessFailure(errorCode = -1, errorMsg = "mock api error"),
                        )
                    }) { Text("API失败") }
                }
            }
            when {
                uiState.isLoading -> item { Text("加载中…") }
                uiState.errorMessage != null -> item {
                    Column {
                        Text(
                            "请求失败：${uiState.errorMessage}",
                            color = Color.Red,
                        )
                        Button(
                            onClick = { store.dispatch(NavIntent.Retry) },
                            modifier = Modifier.padding(top = 8.dp),
                        ) { Text("重试") }
                    }
                }
                else -> {
                    item {
                        Text(
                            "共 ${uiState.categories.size} 个分类",
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                    }
                    itemsIndexed(uiState.categories, key = { index, _ -> index }) { _, nav ->
                        NavCategoryFlowSection(nav) { item ->
                            store.dispatch(NavIntent.ClickItem(item))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NavCategoryFlowSection(nav: Nav, onItemClick: (NavItem) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Text(
            nav.name,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            nav.items.forEach { item ->
                Button(onClick = { onItemClick(item) }) {
                    Text(
                        text = item.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
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
