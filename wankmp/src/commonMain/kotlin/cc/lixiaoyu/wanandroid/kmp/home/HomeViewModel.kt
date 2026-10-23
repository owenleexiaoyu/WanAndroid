package cc.lixiaoyu.wanandroid.kmp.home

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

/** Shared MVI state owner. All dispatches run on the host's main dispatcher. */
class HomeViewModel(
    private val repository: HomeRepository,
    parentScope: CoroutineScope,
    initiallyLoggedIn: Boolean = false,
) {
    private val job = SupervisorJob(parentScope.coroutineContext[Job])
    private val scope = CoroutineScope(parentScope.coroutineContext + job)
    private val mutableState = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = mutableState.asStateFlow()
    private val effects = Channel<HomeEffect>(Channel.BUFFERED)
    val effect = effects.receiveAsFlow()
    private val requests = mutableMapOf<FeedKey, Job>()
    private val generations = mutableMapOf<FeedKey, Int>()
    private val chapterRequests = mutableMapOf<HomeTab, Job>()
    private var loggedIn = initiallyLoggedIn
    private var sessionGeneration = 0
    private val collectionOverrides = mutableMapOf<Int, Boolean>()

    fun dispatch(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.SelectTab -> {
                update { it.copy(selectedTab = intent.tab) }
                ensureTab(intent.tab)
            }
            is HomeIntent.EnsureTab -> ensureTab(intent.tab)
            is HomeIntent.SelectChapter -> {
                update { it.copy(selectedChapters = it.selectedChapters + (intent.tab to intent.id)) }
                ensureFeed(FeedKey(intent.tab, intent.id))
            }
            is HomeIntent.RetryChapters -> loadChapters(intent.tab)
            is HomeIntent.Refresh -> load(intent.key, refresh = true)
            is HomeIntent.LoadMore -> load(intent.key, refresh = false)
            HomeIntent.Search -> emit(HomeEffect.OpenSearch)
            is HomeIntent.OpenArticle -> if (intent.article.link.isNotBlank()) emit(HomeEffect.OpenArticle(intent.article))
            is HomeIntent.ToggleCollect -> toggleCollect(intent.articleId)
            is HomeIntent.CollectionChanged -> setCollected(intent.articleId, intent.collected)
            is HomeIntent.SessionChanged -> sessionChanged(intent.loggedIn)
        }
    }

    private fun update(transform: (HomeState) -> HomeState) { mutableState.update(transform) }
    private fun emit(effect: HomeEffect) { scope.launch { effects.send(effect) } }
    private fun ensureTab(tab: HomeTab) {
        if (tab.hasChapters) {
            val chapters = state.value.chapters[tab]
            if (chapters?.loaded != true) {
                if (chapters?.loading != true && chapters?.error == null) loadChapters(tab)
            } else state.value.selectedChapters[tab]?.let { ensureFeed(FeedKey(tab, it)) }
        } else ensureFeed(FeedKey(tab))
    }
    private fun ensureFeed(key: FeedKey) {
        val feed = state.value.feeds[key]
        if (feed == null || (!feed.initialized && !feed.refreshing && feed.error == null)) load(key, true)
    }
    private fun loadChapters(tab: HomeTab) {
        if (chapterRequests[tab]?.isActive == true) return
        update { it.copy(chapters = it.chapters + (tab to ChaptersState(loading = true))) }
        chapterRequests[tab] = scope.launch {
            try {
                val items = repository.chapters(tab).distinctBy { it.id }
                val selected = state.value.selectedChapters[tab]?.takeIf { id -> items.any { it.id == id } }
                    ?: items.firstOrNull()?.id
                update { it.copy(
                    chapters = it.chapters + (tab to ChaptersState(items = items, loaded = true)),
                    selectedChapters = if (selected == null) it.selectedChapters - tab else it.selectedChapters + (tab to selected),
                ) }
                selected?.let { ensureFeed(FeedKey(tab, it)) }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) {
                update { it.copy(chapters = it.chapters + (tab to ChaptersState(error = e.userMessage("分类加载失败，请检查网络后重试")))) }
            }
        }
    }
    private fun updateFeed(key: FeedKey, transform: (FeedState) -> FeedState) {
        update { it.copy(feeds = it.feeds + (key to transform(it.feeds[key] ?: FeedState(nextPage = key.tab.firstPage)))) }
    }
    private fun load(key: FeedKey, refresh: Boolean) {
        val previous = state.value.feeds[key] ?: FeedState(nextPage = key.tab.firstPage)
        if (!refresh && (!previous.initialized || previous.refreshing || previous.loadingMore || previous.endReached)) return
        requests[key]?.cancel()
        val generation = (generations[key] ?: 0) + 1
        generations[key] = generation
        val page = if (refresh) key.tab.firstPage else previous.nextPage
        updateFeed(key) { it.copy(refreshing = refresh, loadingMore = !refresh, error = null, refreshFailed = false) }
        requests[key] = scope.launch {
            try {
                val (result, pinned) = coroutineScope<Pair<ArticlePage, List<Article>>> {
                    val top: Deferred<List<Article>>? = if (refresh && key.tab == HomeTab.Hot) async {
                        try { repository.topArticles() }
                        catch (e: CancellationException) { throw e }
                        catch (_: Exception) { emptyList() }
                    } else null
                    val result = repository.articles(key, page)
                    result to top?.await().orEmpty()
                }
                if (generations[key] != generation) return@launch
                updateFeed(key) { current ->
                    val articles = ((if (refresh) pinned else current.articles) + result.datas)
                        .distinctBy { it.id }.map { article ->
                            collectionOverrides[article.id]?.let { article.copy(collect = it) } ?: article
                        }
                    current.copy(articles = articles, initialized = true, refreshing = false, loadingMore = false,
                        nextPage = page + 1, endReached = result.over || result.curPage >= result.pageCount, error = null)
                }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) {
                if (generations[key] != generation) return@launch
                updateFeed(key) { it.copy(refreshing = false, loadingMore = false, error = e.userMessage("加载失败，请检查网络后重试"), refreshFailed = refresh) }
                if (e is WanApiException && e.code == -1001) requestLoginAfterExpiry()
            }
        }
    }
    private fun toggleCollect(id: Int) {
        if (!loggedIn) { emit(HomeEffect.RequestLogin); return }
        if (id in state.value.collecting) return
        val article = state.value.feeds.values.asSequence().flatMap { it.articles.asSequence() }.firstOrNull { it.id == id } ?: return
        val session = sessionGeneration
        update { it.copy(collecting = it.collecting + id) }
        scope.launch {
            try {
                repository.collect(id, !article.collect)
                if (session == sessionGeneration) {
                    setCollected(id, !article.collect)
                    HomeCollectionEvents.publish(id, !article.collect)
                    emit(HomeEffect.Message(if (article.collect) "取消收藏成功" else "收藏成功"))
                }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) {
                if (session == sessionGeneration) {
                    if (e is WanApiException && e.code == -1001) requestLoginAfterExpiry()
                    else emit(HomeEffect.Message(e.userMessage("收藏操作失败，请稍后重试")))
                }
            } finally { update { it.copy(collecting = it.collecting - id) } }
        }
    }
    private fun setCollected(id: Int, collected: Boolean) {
        collectionOverrides[id] = collected
        update { it.copy(feeds = it.feeds.mapValues { (_, feed) ->
            feed.copy(articles = feed.articles.map { article -> if (article.id == id) article.copy(collect = collected) else article })
        }) }
    }
    private fun requestLoginAfterExpiry() {
        loggedIn = false
        sessionGeneration++
        collectionOverrides.clear()
        emit(HomeEffect.RequestLogin)
    }
    private fun sessionChanged(value: Boolean) {
        if (loggedIn == value) return
        loggedIn = value
        sessionGeneration++
        collectionOverrides.clear()
        if (!value) update { it.copy(feeds = it.feeds.mapValues { (_, feed) ->
            feed.copy(articles = feed.articles.map { a -> a.copy(collect = false) })
        }) }
        state.value.feeds.keys.toList().forEach { load(it, true) }
    }
    fun close() { job.cancel(); effects.close() }
}

/** Host bridge for collection changes made by legacy Android detail screens. */
object HomeCollectionEvents {
    private val mutableChanges = MutableSharedFlow<Pair<Int, Boolean>>(extraBufferCapacity = 16)
    val changes = mutableChanges.asSharedFlow()
    fun publish(id: Int, collected: Boolean) { mutableChanges.tryEmit(id to collected) }
}

/** Keep transport diagnostics out of the page while preserving API business messages. */
private fun Exception.userMessage(fallback: String): String =
    if (this is WanApiException) message?.takeIf { it.isNotBlank() } ?: fallback else fallback
