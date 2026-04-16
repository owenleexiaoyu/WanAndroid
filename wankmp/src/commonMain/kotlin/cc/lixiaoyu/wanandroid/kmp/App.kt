package cc.lixiaoyu.wanandroid.kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cc.lixiaoyu.wanandroid.kmp.nav.data.dto.Nav
import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.NavRemoteDataSource
import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.createNavigationHttpClient
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * 临时：拉取 navi/json 并展示，验证 KMP 网络与序列化。
 */
@Composable
@Preview
fun App() {
    MaterialTheme {
        val client = remember { createNavigationHttpClient() }
        DisposableEffect(Unit) {
            onDispose { client.close() }
        }
        val dataSource = remember(client) { NavRemoteDataSource(client) }

        var loading by remember { mutableStateOf(true) }
        var errorText by remember { mutableStateOf<String?>(null) }
        var categories by remember { mutableStateOf<List<Nav>>(emptyList()) }

        LaunchedEffect(dataSource) {
            loading = true
            dataSource.fetchNavigation()
                .onSuccess {
                    categories = it
                    errorText = null
                }
                .onFailure {
                    errorText = it.message ?: it.toString()
                    categories = emptyList()
                }
            loading = false
        }

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            item {
                Text(
                    "Nav API（临时测试）",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "GET ${NavRemoteDataSource.NAV_JSON_URL}",
                    style = MaterialTheme.typography.caption,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }
            when {
                loading -> item { Text("加载中…") }
                errorText != null -> item {
                    Text(
                        "请求失败：$errorText",
                        color = Color.Red,
                    )
                }
                else -> {
                    item {
                        Text(
                            "共 ${categories.size} 个分类",
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                    }
                    itemsIndexed(categories, key = { index, _ -> index }) { _, nav ->
                        NavCategoryPreview(nav)
                    }
                }
            }
        }
    }
}

@Composable
private fun NavCategoryPreview(nav: Nav) {
    Text(
        nav.name,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
    )
    val preview = nav.items.take(8)
    preview.forEach { item ->
        Text(
            "· ${item.title}",
            style = MaterialTheme.typography.body2,
            color = Color.DarkGray,
            modifier = Modifier.padding(start = 8.dp, bottom = 2.dp),
        )
    }
    if (nav.items.size > preview.size) {
        Text(
            "… 共 ${nav.items.size} 条",
            style = MaterialTheme.typography.caption,
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}
