package cc.lixiaoyu.wanandroid.kmp.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.pullrefresh.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.lixiaoyu.wanandroid.kmp.core.ui.WanAsyncImage
import cc.lixiaoyu.wanandroid.kmp.theme.wanColors
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, modifier: Modifier = Modifier, onEffect: (HomeEffect) -> Unit) {
    val state by viewModel.state.collectAsState()
    val currentOnEffect by rememberUpdatedState(onEffect)
    val tabs = HomeTab.values()
    val pager = rememberPagerState(initialPage = state.selectedTab.ordinal) { tabs.size }
    val scope = rememberCoroutineScope()
    val scrollRequests = remember { MutableSharedFlow<FeedKey>(extraBufferCapacity = 1) }
    val saveable = rememberSaveableStateHolder()
    LaunchedEffect(viewModel) { viewModel.effect.collect { currentOnEffect(it) } }
    LaunchedEffect(pager, viewModel) {
        snapshotFlow { pager.currentPage }.distinctUntilChanged().collect {
            viewModel.dispatch(HomeIntent.SelectTab(tabs[it]))
        }
    }
    fun scrollCurrent(tab: HomeTab) {
        val chapter = state.selectedChapters[tab]
        if (!tab.hasChapters || chapter != null) scrollRequests.tryEmit(FeedKey(tab, chapter))
    }
    Column(modifier.fillMaxSize().background(MaterialTheme.wanColors.surface)) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth().height(44.dp)
                .background(MaterialTheme.wanColors.pageBackground, RoundedCornerShape(24.dp))
                .clickable { viewModel.dispatch(HomeIntent.Search) }.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Default.Search, null, tint = MaterialTheme.wanColors.textSecondary)
            Spacer(Modifier.width(10.dp))
            Text("搜索关键词以空格隔开", color = MaterialTheme.wanColors.textSecondary, fontSize = 16.sp)
        }
        TabRow(selectedTabIndex = pager.currentPage, backgroundColor = MaterialTheme.wanColors.surface,
            contentColor = MaterialTheme.wanColors.textPrimary) {
            tabs.forEachIndexed { index, tab ->
                Tab(selected = pager.currentPage == index, onClick = {
                    if (pager.currentPage == index && !pager.isScrollInProgress) scrollCurrent(tab)
                    else scope.launch { pager.animateScrollToPage(index) }
                }, text = { Text(tab.title) })
            }
        }
        HorizontalPager(state = pager, modifier = Modifier.weight(1f).fillMaxWidth(), key = { tabs[it].name }) { index ->
            val tab = tabs[index]
            if (tab.hasChapters) {
                val chapters = state.chapters[tab] ?: ChaptersState()
                Column(Modifier.fillMaxSize()) {
                    when {
                        chapters.loading -> StatusPanel("正在加载分类…", loading = true)
                        chapters.error != null -> StatusPanel(chapters.error, action = "重试") {
                            viewModel.dispatch(HomeIntent.RetryChapters(tab))
                        }
                        chapters.loaded && chapters.items.isEmpty() -> StatusPanel("暂无分类")
                        chapters.items.isNotEmpty() -> {
                            val selected = chapters.items.indexOfFirst { it.id == state.selectedChapters[tab] }.coerceAtLeast(0)
                            ChapterCapsuleTabs(chapters.items, chapters.items[selected].id) { chapter ->
                                if (chapter.id == chapters.items[selected].id) scrollRequests.tryEmit(FeedKey(tab, chapter.id))
                                else viewModel.dispatch(HomeIntent.SelectChapter(tab, chapter.id))
                            }
                            // Categories switch only on click; horizontal gestures belong to the main pager.
                            val key = FeedKey(tab, chapters.items[selected].id)
                            Box(Modifier.weight(1f).fillMaxWidth()) {
                                saveable.SaveableStateProvider("${tab.name}:${key.chapterId}") {
                                    ArticleFeed(key, state, viewModel, scrollRequests)
                                }
                            }
                        }
                    }
                }
            } else {
                saveable.SaveableStateProvider(tab.name) {
                    ArticleFeed(FeedKey(tab), state, viewModel, scrollRequests)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ArticleFeed(key: FeedKey, state: HomeState, viewModel: HomeViewModel, scrollRequests: Flow<FeedKey>) {
    ArticleList(
        feed = state.feeds[key] ?: FeedState(),
        style = when (key.tab) {
            HomeTab.Hot -> ArticleCellStyle.HeaderAuthor
            HomeTab.Project -> ArticleCellStyle.Project
            HomeTab.Square, HomeTab.WeChat -> ArticleCellStyle.FooterAuthor
        }, collecting = state.collecting,
        active = state.selectedTab == key.tab && (!key.tab.hasChapters || state.selectedChapters[key.tab] == key.chapterId),
        scrollRequests = remember(key, scrollRequests) { scrollRequests.filter { it == key }.map { Unit } },
        onRefresh = { viewModel.dispatch(HomeIntent.Refresh(key)) },
        onLoadMore = { viewModel.dispatch(HomeIntent.LoadMore(key)) },
        onArticle = { viewModel.dispatch(HomeIntent.OpenArticle(it)) },
        onCollect = { viewModel.dispatch(HomeIntent.ToggleCollect(it)) },
    )
}

/** Shared pagination, refresh and cell presentation for home and knowledge feeds. */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticleList(
    feed: FeedState,
    style: ArticleCellStyle,
    collecting: Set<Int>,
    active: Boolean,
    scrollRequests: Flow<Unit>,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onArticle: (Article) -> Unit,
    onCollect: (Int) -> Unit,
) {
    val currentOnRefresh by rememberUpdatedState(onRefresh)
    val currentOnLoadMore by rememberUpdatedState(onLoadMore)
    val listState = rememberLazyListState()
    val currentFeed by rememberUpdatedState(feed)
    LaunchedEffect(listState, scrollRequests) {
        scrollRequests.collectLatest { listState.animateScrollToItem(0) }
    }
    // Only the visible list can request another page. Layout changes after a page is
    // appended re-evaluate the threshold, including short/filtered API pages.
    LaunchedEffect(listState, active) {
        if (!active) return@LaunchedEffect
        snapshotFlow {
            val data = currentFeed
            val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            Triple(last >= data.articles.size - 4, data.articles.size, data.loadingMore || data.refreshing)
        }.collect { (nearEnd, _, busy) ->
            val data = currentFeed
            if (nearEnd && !busy && data.initialized && !data.endReached && data.error == null) {
                currentOnLoadMore()
            }
        }
    }
    val refresh = rememberPullRefreshState(feed.refreshing, { currentOnRefresh() })
    Box(Modifier.fillMaxSize().pullRefresh(refresh).background(MaterialTheme.wanColors.pageBackground)) {
        LazyColumn(state = listState, modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(top = 8.dp)) {
            if (feed.articles.isEmpty()) item {
                when {
                    feed.refreshing || !feed.initialized && feed.error == null -> StatusPanel("正在加载…", loading = true)
                    feed.error != null -> StatusPanel(feed.error, "重试") { currentOnRefresh() }
                    else -> StatusPanel("暂无文章", "刷新") { currentOnRefresh() }
                }
            }
            items(feed.articles, key = { it.id }) { article ->
                ArticleCell(article, style, article.id in collecting,
                    onClick = { onArticle(article) }, onCollect = { onCollect(article.id) })
                Spacer(Modifier.height(8.dp))
            }
            if (feed.articles.isNotEmpty()) item {
                when {
                    feed.loadingMore -> StatusPanel("正在加载更多…", loading = true)
                    feed.error != null -> StatusPanel(feed.error, "重试") {
                        if (feed.refreshFailed || !feed.initialized) currentOnRefresh() else currentOnLoadMore()
                    }
                    feed.endReached -> StatusPanel("没有更多了")
                }
            }
        }
        PullRefreshIndicator(feed.refreshing, refresh, Modifier.align(Alignment.TopCenter))
    }
}

enum class ArticleCellStyle { HeaderAuthor, FooterAuthor, Project }

/** One cell, configured by presentation rather than a page's identity. */
@Composable
fun ArticleCell(article: Article, style: ArticleCellStyle, collecting: Boolean, onClick: () -> Unit, onCollect: () -> Unit) {
    val authorAtBottom = style == ArticleCellStyle.FooterAuthor
    val project = style == ArticleCellStyle.Project
    val author = article.displayAuthor.plainText()
    val category = if (authorAtBottom) "" else article.displayCategory
    val colors = MaterialTheme.wanColors
    Column(Modifier.fillMaxWidth().background(colors.surface).clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 10.dp)) {
        if (article.type == 1 || !authorAtBottom && author.isNotEmpty() || category.isNotEmpty()) {
            Row(Modifier.fillMaxWidth().padding(bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                if (article.type == 1) Text("置顶", color = Color(0xFFE53935), fontSize = 13.sp, modifier = Modifier.padding(end = 8.dp))
                if (!authorAtBottom && author.isNotEmpty()) Text(author, color = colors.textPrimary, fontSize = 13.sp,
                    maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                else Spacer(Modifier.weight(1f))
                if (category.isNotEmpty()) Text(category, color = colors.textSecondary, fontSize = 12.sp,
                    maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f).padding(start = 8.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End)
            }
        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text(article.title.plainText(), color = colors.textPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium,
                    maxLines = 2, overflow = TextOverflow.Ellipsis)
                if (project && !article.desc.isNullOrBlank()) Text(article.desc.plainText(), color = colors.textSecondary,
                    fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 4.dp))
            }
            if (project) {
                Spacer(Modifier.width(12.dp))
                Box(Modifier.size(80.dp).background(colors.pageBackground, RoundedCornerShape(4.dp))) {
                    Text("图片", color = colors.textSecondary, fontSize = 12.sp, modifier = Modifier.align(Alignment.Center))
                    if (!article.envelopePic.isNullOrBlank()) WanAsyncImage(article.envelopePic, "项目图片", Modifier.fillMaxSize(), ContentScale.Crop)
                }
            }
        }
        Row(Modifier.fillMaxWidth().padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            if (authorAtBottom && author.isNotEmpty()) Text(author, color = colors.textPrimary, fontSize = 13.sp,
                maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.widthIn(max = 140.dp).padding(end = 8.dp))
            Text(article.displayTime, color = colors.textSecondary, fontSize = 12.sp,
                maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
            IconButton(onClick = onCollect, enabled = !collecting, modifier = Modifier.size(40.dp)) {
                if (collecting) CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
                else Icon(if (article.collect) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    if (article.collect) "取消收藏" else "收藏", modifier = Modifier.size(20.dp), tint = if (article.collect) colors.brand else colors.textSecondary)
            }
        }
    }
}

@Composable
private fun StatusPanel(text: String, action: String? = null, loading: Boolean = false, onAction: () -> Unit = {}) {
    Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        if (loading) CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
        Text(text, color = MaterialTheme.wanColors.textSecondary, modifier = Modifier.padding(top = 8.dp))
        if (action != null) TextButton(onClick = onAction) { Text(action) }
    }
}
