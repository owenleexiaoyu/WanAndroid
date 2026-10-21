package cc.lixiaoyu.wanandroid.kmp.home

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private class FakeRepository : HomeRepository {
        val calls = mutableListOf<Pair<FeedKey, Int>>()
        var topFails = false
        var failure = false
        var articleFailure: Exception? = null
        var blocked: CompletableDeferred<Unit>? = null
        var collectFailure: Exception? = null
        var collectCalls = 0
        override suspend fun chapters(tab: HomeTab) = listOf(Chapter(42, "分类"), Chapter(43, "其他"))
        override suspend fun articles(key: FeedKey, page: Int): ArticlePage {
            calls += key to page
            blocked?.await()
            articleFailure?.let { throw it }
            if (failure) error("断网")
            return ArticlePage(listOf(Article(page + 1, "标题", "https://example.com")), page + 1, 3, page >= key.tab.firstPage + 1)
        }
        override suspend fun topArticles(): List<Article> {
            if (topFails) error("置顶失败")
            return listOf(Article(1, "置顶", "https://example.com", type = 1))
        }
        override suspend fun collect(id: Int, collected: Boolean) {
            collectCalls++
            blocked?.await()
            collectFailure?.let { throw it }
        }
    }
    @Test fun firstPagesCategoriesCacheAndTopDeduplication() = runTest {
        val repository = FakeRepository()
        val vm = HomeViewModel(repository, this)
        try {
            vm.dispatch(HomeIntent.SelectTab(HomeTab.Hot)); advanceUntilIdle()
            assertEquals(1, vm.state.value.feeds.getValue(FeedKey(HomeTab.Hot)).articles.size)
            assertEquals(1, vm.state.value.feeds.getValue(FeedKey(HomeTab.Hot)).articles.first().type)
            vm.dispatch(HomeIntent.SelectTab(HomeTab.Project)); advanceUntilIdle()
            vm.dispatch(HomeIntent.SelectChapter(HomeTab.Project, 43)); advanceUntilIdle()
            vm.dispatch(HomeIntent.SelectTab(HomeTab.WeChat)); advanceUntilIdle()
            vm.dispatch(HomeIntent.SelectTab(HomeTab.Hot)); advanceUntilIdle()
            assertEquals(listOf(FeedKey(HomeTab.Hot) to 0, FeedKey(HomeTab.Project, 42) to 1,
                FeedKey(HomeTab.Project, 43) to 1, FeedKey(HomeTab.WeChat, 42) to 1), repository.calls)
        } finally { vm.close() }
    }
    @Test fun pagingFailureRetriesSamePageAndStopsAtEnd() = runTest {
        val repository = FakeRepository().apply { topFails = true }
        val vm = HomeViewModel(repository, this)
        val key = FeedKey(HomeTab.Hot)
        try {
            vm.dispatch(HomeIntent.SelectTab(HomeTab.Hot)); advanceUntilIdle()
            assertEquals(1, vm.state.value.feeds.getValue(key).articles.size)
            repository.failure = true
            vm.dispatch(HomeIntent.LoadMore(key)); advanceUntilIdle()
            assertEquals(1, vm.state.value.feeds.getValue(key).nextPage)
            repository.failure = false
            vm.dispatch(HomeIntent.LoadMore(key)); advanceUntilIdle()
            vm.dispatch(HomeIntent.LoadMore(key)); advanceUntilIdle()
            assertEquals(listOf(0, 1, 1), repository.calls.map { it.second })
            assertTrue(vm.state.value.feeds.getValue(key).endReached)
        } finally { vm.close() }
    }
    @Test fun refreshCancelsOlderRequestAndDuplicateLoadsAreIgnored() = runTest {
        val repository = FakeRepository().apply { blocked = CompletableDeferred() }
        val vm = HomeViewModel(repository, this)
        val key = FeedKey(HomeTab.Square)
        try {
            vm.dispatch(HomeIntent.SelectTab(HomeTab.Square)); runCurrent()
            vm.dispatch(HomeIntent.EnsureTab(HomeTab.Square))
            vm.dispatch(HomeIntent.LoadMore(key)); runCurrent()
            assertEquals(1, repository.calls.size)
            vm.dispatch(HomeIntent.Refresh(key)); runCurrent()
            repository.blocked!!.complete(Unit); advanceUntilIdle()
            assertEquals(listOf(0, 0), repository.calls.map { it.second })
            assertFalse(vm.state.value.feeds.getValue(key).refreshing)
            assertEquals(1, vm.state.value.feeds.getValue(key).articles.size)
        } finally { vm.close() }
    }
    @Test fun collectionLoginGuardFailureAndCrossFeedSync() = runTest {
        val repository = FakeRepository()
        val vm = HomeViewModel(repository, this)
        try {
            vm.dispatch(HomeIntent.SelectTab(HomeTab.Hot)); advanceUntilIdle()
            vm.dispatch(HomeIntent.SelectTab(HomeTab.Square)); advanceUntilIdle()
            vm.dispatch(HomeIntent.ToggleCollect(1))
            assertEquals(HomeEffect.RequestLogin, vm.effect.first())
            assertEquals(0, repository.collectCalls)
            vm.dispatch(HomeIntent.SessionChanged(true)); advanceUntilIdle()
            repository.blocked = CompletableDeferred()
            vm.dispatch(HomeIntent.ToggleCollect(1)); runCurrent()
            vm.dispatch(HomeIntent.ToggleCollect(1)); runCurrent()
            assertEquals(1, repository.collectCalls)
            repository.blocked!!.complete(Unit); advanceUntilIdle()
            assertTrue(vm.state.value.feeds.values.all { it.articles.first().collect })
            repository.collectFailure = IllegalStateException("失败")
            vm.dispatch(HomeIntent.ToggleCollect(1)); advanceUntilIdle()
            assertTrue(vm.state.value.feeds.values.all { it.articles.first().collect })
            assertTrue(vm.state.value.collecting.isEmpty())
            vm.dispatch(HomeIntent.CollectionChanged(1, false))
            assertTrue(vm.state.value.feeds.values.all { !it.articles.first().collect })
        } finally { vm.close() }
    }
    @Test fun expiredSessionReauthenticationRefreshesWithoutRepeatingCollect() = runTest {
        val repository = FakeRepository()
        val vm = HomeViewModel(repository, this, initiallyLoggedIn = true)
        try {
            vm.dispatch(HomeIntent.SelectTab(HomeTab.Hot)); advanceUntilIdle()
            repository.collectFailure = WanApiException(-1001, "登录过期")
            vm.dispatch(HomeIntent.ToggleCollect(1)); runCurrent()
            assertEquals(HomeEffect.RequestLogin, vm.effect.first())
            assertFalse(vm.state.value.feeds.getValue(FeedKey(HomeTab.Hot)).articles.first().collect)
            vm.dispatch(HomeIntent.SessionChanged(true)); advanceUntilIdle()
            assertEquals(2, repository.calls.size)
            assertEquals(1, repository.collectCalls)
        } finally { vm.close() }
    }
    @Test fun closeCancelsPendingRequests() = runTest {
        val repository = FakeRepository().apply { blocked = CompletableDeferred() }
        val vm = HomeViewModel(repository, this)
        vm.dispatch(HomeIntent.SelectTab(HomeTab.Hot)); runCurrent()
        vm.close()
        repository.blocked!!.complete(Unit); advanceUntilIdle()
        assertTrue(vm.state.value.feeds.getValue(FeedKey(HomeTab.Hot)).articles.isEmpty())
    }

    @Test fun transportErrorsRemainReadableAndRetryRecovers() = runTest {
        val repository = FakeRepository().apply {
            articleFailure = IllegalStateException("NSURL error: " + "TLS diagnostic ".repeat(200))
        }
        val vm = HomeViewModel(repository, this)
        val key = FeedKey(HomeTab.Square)
        try {
            vm.dispatch(HomeIntent.SelectTab(HomeTab.Square)); advanceUntilIdle()
            assertEquals("加载失败，请检查网络后重试", vm.state.value.feeds.getValue(key).error)
            assertTrue(vm.state.value.feeds.getValue(key).refreshFailed)
            repository.articleFailure = WanApiException(-1, "维护中")
            vm.dispatch(HomeIntent.Refresh(key)); advanceUntilIdle()
            assertEquals("维护中", vm.state.value.feeds.getValue(key).error)
            repository.articleFailure = null
            vm.dispatch(HomeIntent.Refresh(key)); advanceUntilIdle()
            assertNull(vm.state.value.feeds.getValue(key).error)
            assertTrue(vm.state.value.feeds.getValue(key).initialized)
        } finally { vm.close() }
    }

}
