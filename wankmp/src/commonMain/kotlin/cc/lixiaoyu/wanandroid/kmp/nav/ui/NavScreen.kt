package cc.lixiaoyu.wanandroid.kmp.nav.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.lixiaoyu.wanandroid.kmp.nav.data.dto.Nav
import cc.lixiaoyu.wanandroid.kmp.nav.data.dto.NavItem
import cc.lixiaoyu.wanandroid.kmp.nav.mvi.NavEffect
import cc.lixiaoyu.wanandroid.kmp.nav.mvi.NavIntent
import cc.lixiaoyu.wanandroid.kmp.nav.mvi.NavStore
import cc.lixiaoyu.wanandroid.kmp.nav.util.decodeHtmlEntities
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * 导航页：顶栏「导航」+ 纵向分类列表，每类下为流式标签（与设计稿一致）。
 *
 * @param onOpenUrl 消费 [NavEffect.OpenUrl]（由宿主决定 Toast / WebView / 浏览器等）。
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NavScreen(
    store: NavStore,
    modifier: Modifier = Modifier,
    showTopBar: Boolean = true,
    title: String = "导航",
    onOpenUrl: (NavEffect.OpenUrl) -> Unit,
) {
    val uiState by store.state.collectAsState()

    LaunchedEffect(store) {
        coroutineScope {
            launch {
                store.effect.collect { effect ->
                    if (effect is NavEffect.OpenUrl) onOpenUrl(effect)
                }
            }
            store.dispatch(NavIntent.Enter)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Medium,
                        )
                    },
                    backgroundColor = Color.White,
                    elevation = 1.dp,
                )
            }
        },
        backgroundColor = Color.White,
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                uiState.errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = uiState.errorMessage.orEmpty(),
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.body1,
                        )
                        TextButton(
                            onClick = { store.dispatch(NavIntent.Retry) },
                            modifier = Modifier.padding(top = 16.dp),
                        ) {
                            Text("重试")
                        }
                    }
                }
                uiState.categories.isEmpty() -> {
                    Text(
                        text = "暂无数据",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray,
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        itemsIndexed(
                            uiState.categories,
                            key = { index, _ -> index },
                        ) { _, category ->
                            NavCategorySection(
                                category = category,
                                onItemClick = { store.dispatch(NavIntent.ClickItem(it)) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NavCategorySection(
    category: Nav,
    onItemClick: (NavItem) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        Text(
            text = category.name.decodeHtmlEntities(),
            style = MaterialTheme.typography.body2,
            color = Color(0xFF888888),
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            category.items.forEach { item ->
                NavLinkChip(title = item.title.decodeHtmlEntities(), onClick = { onItemClick(item) })
            }
        }
    }
}

@Composable
private fun NavLinkChip(
    title: String,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .clip(shape)
            .background(color = Color(0xFFEEEEEE), shape = shape)
            .clickable(onClick = onClick),
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.body2,
            color = Color(0xFF333333),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
