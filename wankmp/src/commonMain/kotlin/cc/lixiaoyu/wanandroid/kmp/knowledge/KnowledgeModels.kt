package cc.lixiaoyu.wanandroid.kmp.knowledge

import cc.lixiaoyu.wanandroid.kmp.home.*

data class KnowledgeState(
    val chapters: List<Chapter> = emptyList(),
    val loading: Boolean = false,
    val loaded: Boolean = false,
    val error: String? = null,
    val selectedParentId: Int? = null,
    val selectedChildren: Map<Int, Int> = emptyMap(),
    val feeds: Map<Int, FeedState> = emptyMap(),
    val collecting: Set<Int> = emptySet(),
    val filterVisible: Boolean = false,
)

sealed interface KnowledgeIntent {
    object Enter : KnowledgeIntent
    object Retry : KnowledgeIntent
    data class SelectParent(val id: Int) : KnowledgeIntent
    data class SelectChild(val parentId: Int, val childId: Int) : KnowledgeIntent
    data class Filter(val visible: Boolean) : KnowledgeIntent
    data class Refresh(val id: Int) : KnowledgeIntent
    data class LoadMore(val id: Int) : KnowledgeIntent
    data class OpenArticle(val article: Article) : KnowledgeIntent
    data class ToggleCollect(val id: Int) : KnowledgeIntent
    data class SessionChanged(val loggedIn: Boolean) : KnowledgeIntent
    data class CollectionChanged(val id: Int, val collected: Boolean) : KnowledgeIntent
}

sealed interface KnowledgeEffect {
    data class OpenArticle(val article: Article) : KnowledgeEffect
    object RequestLogin : KnowledgeEffect
    data class Message(val text: String) : KnowledgeEffect
}
