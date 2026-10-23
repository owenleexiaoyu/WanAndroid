package cc.lixiaoyu.wanandroid.kmp.knowledge

import cc.lixiaoyu.wanandroid.kmp.home.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class KnowledgeViewModel(
    private val repository: KnowledgeRepository,
    parentScope: CoroutineScope,
    initiallyLoggedIn: Boolean = false,
) {
    private val job = SupervisorJob(parentScope.coroutineContext[Job])
    private val scope = CoroutineScope(parentScope.coroutineContext + job)
    private val mutableState = MutableStateFlow(KnowledgeState())
    val state = mutableState.asStateFlow()
    private val effects = Channel<KnowledgeEffect>(Channel.BUFFERED)
    val effect = effects.receiveAsFlow()
    private var treeRequest: Job? = null
    private val requests = mutableMapOf<Int, Job>()
    private val generations = mutableMapOf<Int, Int>()
    private val collectionOverrides = mutableMapOf<Int, Boolean>()
    private var loggedIn = initiallyLoggedIn
    private var session = 0

    fun dispatch(intent: KnowledgeIntent) {
        when (intent) {
            KnowledgeIntent.Enter -> if (!state.value.loaded && state.value.error == null) loadTree()
            KnowledgeIntent.Retry -> loadTree()
            is KnowledgeIntent.Filter -> update { it.copy(filterVisible = intent.visible) }
            is KnowledgeIntent.SelectParent -> select(intent.id, null)
            is KnowledgeIntent.SelectChild -> select(intent.parentId, intent.childId)
            is KnowledgeIntent.Refresh -> load(intent.id, true)
            is KnowledgeIntent.LoadMore -> load(intent.id, false)
            is KnowledgeIntent.OpenArticle -> if (intent.article.link.isNotBlank()) emit(KnowledgeEffect.OpenArticle(intent.article))
            is KnowledgeIntent.ToggleCollect -> collect(intent.id)
            is KnowledgeIntent.CollectionChanged -> setCollected(intent.id, intent.collected)
            is KnowledgeIntent.SessionChanged -> {
                if (loggedIn != intent.loggedIn) {
                    loggedIn = intent.loggedIn
                    session++
                    collectionOverrides.clear()
                    if (!loggedIn) update { s -> s.copy(feeds = s.feeds.mapValues { (_, f) ->
                        f.copy(articles = f.articles.map { it.copy(collect = false) })
                    }) }
                    state.value.feeds.keys.toList().forEach { load(it, true) }
                }
            }
        }
    }
    private fun update(transform: (KnowledgeState) -> KnowledgeState) = mutableState.update(transform)
    private fun emit(effect: KnowledgeEffect) { scope.launch { effects.send(effect) } }
    private fun loadTree() {
        if (treeRequest?.isActive == true) return
        update { it.copy(loading = true, error = null) }
        treeRequest = scope.launch {
            try {
                val chapters = repository.tree().filter { it.children.isNotEmpty() }
                val parent = chapters.firstOrNull { it.id == state.value.selectedParentId } ?: chapters.firstOrNull()
                update { it.copy(chapters = chapters, loading = false, loaded = true, selectedParentId = parent?.id) }
                parent?.let { select(it.id, null) }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { update { it.copy(loading = false, error = e.messageForUser("分类加载失败，请重试")) } }
        }
    }
    private fun select(parentId: Int, childId: Int?) {
        val parent = state.value.chapters.firstOrNull { it.id == parentId } ?: return
        val id = childId ?: state.value.selectedChildren[parentId] ?: parent.children.firstOrNull()?.id ?: return
        if (parent.children.none { it.id == id }) return
        update { it.copy(selectedParentId = parentId, selectedChildren = it.selectedChildren + (parentId to id),
            filterVisible = if (childId != null) false else it.filterVisible) }
        val feed = state.value.feeds[id]
        if (feed == null || !feed.initialized && !feed.refreshing && feed.error == null) load(id, true)
    }
    private fun updateFeed(id: Int, transform: (FeedState) -> FeedState) {
        update { it.copy(feeds = it.feeds + (id to transform(it.feeds[id] ?: FeedState()))) }
    }
    private fun load(id: Int, refresh: Boolean) {
        if (state.value.chapters.none { parent -> parent.children.any { it.id == id } }) return
        val previous = state.value.feeds[id] ?: FeedState()
        if (!refresh && (!previous.initialized || previous.refreshing || previous.loadingMore || previous.endReached)) return
        requests[id]?.cancel()
        val generation = (generations[id] ?: 0) + 1
        generations[id] = generation
        val page = if (refresh) 0 else previous.nextPage
        updateFeed(id) { it.copy(refreshing = refresh, loadingMore = !refresh, error = null, refreshFailed = false) }
        requests[id] = scope.launch {
            try {
                val result = repository.articles(id, page)
                if (generations[id] != generation) return@launch
                updateFeed(id) { current -> current.copy(
                    articles = ((if (refresh) emptyList() else current.articles) + result.datas).distinctBy { it.id }
                        .map { a -> collectionOverrides[a.id]?.let { a.copy(collect = it) } ?: a },
                    initialized = true, refreshing = false, loadingMore = false, nextPage = page + 1,
                    endReached = result.over || result.curPage >= result.pageCount, error = null,
                ) }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) {
                if (generations[id] != generation) return@launch
                updateFeed(id) { it.copy(refreshing = false, loadingMore = false, refreshFailed = refresh,
                    error = e.messageForUser("加载失败，请检查网络后重试")) }
                if (e is WanApiException && e.code == -1001) expire()
            }
        }
    }
    private fun collect(id: Int) {
        if (!loggedIn) { emit(KnowledgeEffect.RequestLogin); return }
        if (id in state.value.collecting) return
        val article = state.value.feeds.values.flatMap { it.articles }.firstOrNull { it.id == id } ?: return
        val currentSession = session
        update { it.copy(collecting = it.collecting + id) }
        scope.launch {
            try {
                repository.collect(id, !article.collect)
                if (session == currentSession) {
                    setCollected(id, !article.collect)
                    HomeCollectionEvents.publish(id, !article.collect)
                    emit(KnowledgeEffect.Message(if (article.collect) "取消收藏成功" else "收藏成功"))
                }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) {
                if (session == currentSession) {
                    if (e is WanApiException && e.code == -1001) expire()
                    else emit(KnowledgeEffect.Message(e.messageForUser("收藏操作失败，请重试")))
                }
            } finally { update { it.copy(collecting = it.collecting - id) } }
        }
    }
    private fun expire() { loggedIn = false; session++; collectionOverrides.clear(); emit(KnowledgeEffect.RequestLogin) }
    private fun setCollected(id: Int, collected: Boolean) {
        collectionOverrides[id] = collected
        update { s -> s.copy(feeds = s.feeds.mapValues { (_, feed) ->
            feed.copy(articles = feed.articles.map { if (it.id == id) it.copy(collect = collected) else it })
        }) }
    }
    fun close() { job.cancel(); effects.close() }
}

private fun Exception.messageForUser(fallback: String) =
    if (this is WanApiException) message?.takeIf { it.isNotBlank() } ?: fallback else fallback
