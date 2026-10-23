package cc.lixiaoyu.wanandroid.kmp.knowledge

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cc.lixiaoyu.wanandroid.kmp.home.*
import cc.lixiaoyu.wanandroid.kmp.theme.wanColors
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
fun KnowledgeScreen(viewModel: KnowledgeViewModel, onEffect: (KnowledgeEffect) -> Unit) {
    val state by viewModel.state.collectAsState()
    val currentOnEffect by rememberUpdatedState(onEffect)
    LaunchedEffect(viewModel) {
        viewModel.dispatch(KnowledgeIntent.Enter)
        viewModel.effect.collect { currentOnEffect(it) }
    }
    val scrollRequests = remember { MutableSharedFlow<Int>(extraBufferCapacity = 1) }
    val saveable = rememberSaveableStateHolder()
    Column(Modifier.fillMaxSize().background(MaterialTheme.wanColors.surface)) {
        Box(Modifier.fillMaxWidth().height(56.dp)) {
            Text("体系", color = MaterialTheme.wanColors.textPrimary, style = MaterialTheme.typography.h6,
                modifier = Modifier.align(Alignment.Center))
            IconButton(onClick = { viewModel.dispatch(KnowledgeIntent.Filter(true)) },
                enabled = state.chapters.isNotEmpty(), modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(Icons.Outlined.FilterAlt, "筛选分类", tint = MaterialTheme.wanColors.textPrimary)
            }
        }
        when {
            state.loading -> KnowledgeStatus("正在加载分类…", loading = true)
            state.error != null -> KnowledgeStatus(state.error.orEmpty()) { viewModel.dispatch(KnowledgeIntent.Retry) }
            state.loaded && state.chapters.isEmpty() -> KnowledgeStatus("暂无分类")
            state.chapters.isNotEmpty() -> {
                val selectedIndex = state.chapters.indexOfFirst { it.id == state.selectedParentId }.coerceAtLeast(0)
                val pager = rememberPagerState(initialPage = selectedIndex) { state.chapters.size }
                var programmaticScroll by remember { mutableStateOf(false) }
                LaunchedEffect(state.selectedParentId) {
                    if (pager.currentPage != selectedIndex) {
                        programmaticScroll = true
                        try { pager.scrollToPage(selectedIndex) }
                        finally { programmaticScroll = false }
                    }
                }
                LaunchedEffect(pager, viewModel) {
                    snapshotFlow { if (pager.isScrollInProgress && !programmaticScroll) pager.currentPage else null }
                        .filterNotNull().distinctUntilChanged().collect { index ->
                            viewModel.dispatch(KnowledgeIntent.SelectParent(state.chapters[index].id))
                        }
                }
                ScrollableTabRow(selectedTabIndex = selectedIndex, edgePadding = 8.dp,
                    backgroundColor = MaterialTheme.wanColors.surface, contentColor = MaterialTheme.wanColors.textPrimary) {
                    state.chapters.forEachIndexed { index, parent ->
                        Tab(selected = selectedIndex == index, text = { Text(parent.name.plainText()) }, onClick = {
                            if (state.selectedParentId == parent.id) state.selectedChildren[parent.id]?.let { scrollRequests.tryEmit(it) }
                            else viewModel.dispatch(KnowledgeIntent.SelectParent(parent.id))
                        })
                    }
                }
                HorizontalPager(pager, modifier = Modifier.weight(1f).fillMaxWidth(), key = { state.chapters[it].id }) { index ->
                    val parent = state.chapters[index]
                    val selectedChild = state.selectedChildren[parent.id] ?: parent.children.first().id
                    Column(Modifier.fillMaxSize()) {
                        ChapterCapsuleTabs(parent.children, selectedChild) { child ->
                            if (child.id == selectedChild) scrollRequests.tryEmit(child.id)
                            else viewModel.dispatch(KnowledgeIntent.SelectChild(parent.id, child.id))
                        }
                        saveable.SaveableStateProvider(selectedChild) {
                            ArticleList(feed = state.feeds[selectedChild] ?: FeedState(), style = ArticleCellStyle.FooterAuthor,
                                collecting = state.collecting, active = state.selectedParentId == parent.id,
                                scrollRequests = remember(selectedChild, scrollRequests) { scrollRequests.filter { it == selectedChild }.map { Unit } },
                                onRefresh = { viewModel.dispatch(KnowledgeIntent.Refresh(selectedChild)) },
                                onLoadMore = { viewModel.dispatch(KnowledgeIntent.LoadMore(selectedChild)) },
                                onArticle = { viewModel.dispatch(KnowledgeIntent.OpenArticle(it)) },
                                onCollect = { viewModel.dispatch(KnowledgeIntent.ToggleCollect(it)) })
                        }
                    }
                }
            }
        }
    }
    if (state.filterVisible) KnowledgeFilterSheet(state,
        onDismiss = { viewModel.dispatch(KnowledgeIntent.Filter(false)) },
        onSelect = { parent, child -> viewModel.dispatch(KnowledgeIntent.SelectChild(parent, child)) })
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
private fun KnowledgeFilterSheet(state: KnowledgeState, onDismiss: () -> Unit, onSelect: (Int, Int) -> Unit) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val sheetHeight = maxHeight * 0.9f
            val sheet = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded, skipHalfExpanded = true,
                confirmValueChange = { value ->
                    if (value == ModalBottomSheetValue.Hidden) onDismiss()
                    true
                })
            ModalBottomSheetLayout(sheetState = sheet, sheetBackgroundColor = MaterialTheme.wanColors.surface,
                sheetShape = androidx.compose.foundation.shape.RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                sheetContent = {
                    Column(Modifier.fillMaxWidth().height(sheetHeight)) {
                        Text("选择分类", color = MaterialTheme.wanColors.textPrimary,
                            fontWeight = FontWeight.Medium, modifier = Modifier.padding(20.dp))
                        LazyColumn(Modifier.fillMaxWidth().weight(1f), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                            items(state.chapters, key = { it.id }) { parent ->
                                Text(parent.name.plainText(), color = MaterialTheme.wanColors.textPrimary,
                                    modifier = Modifier.padding(vertical = 12.dp))
                                FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    parent.children.forEach { child ->
                                        ChapterCapsule(child, state.selectedParentId == parent.id && state.selectedChildren[parent.id] == child.id) {
                                            onSelect(parent.id, child.id)
                                        }
                                    }
                                }
                                Divider(Modifier.padding(top = 16.dp, bottom = 8.dp), color = MaterialTheme.wanColors.divider)
                            }
                        }
                    }
                }) { Box(Modifier.fillMaxSize()) }
        }
    }
}

@Composable
private fun KnowledgeStatus(text: String, loading: Boolean = false, onRetry: (() -> Unit)? = null) {
    Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        if (loading) CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
        Text(text, color = MaterialTheme.wanColors.textSecondary, modifier = Modifier.padding(top = 8.dp))
        onRetry?.let { TextButton(onClick = it) { Text("重试") } }
    }
}
