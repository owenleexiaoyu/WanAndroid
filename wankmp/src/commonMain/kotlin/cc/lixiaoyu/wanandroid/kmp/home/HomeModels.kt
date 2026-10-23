package cc.lixiaoyu.wanandroid.kmp.home

import cc.lixiaoyu.wanandroid.kmp.nav.util.decodeHtmlEntities
import kotlinx.serialization.Serializable

@Serializable
data class WanResponse<T>(val data: T? = null, val errorCode: Int = 0, val errorMsg: String = "")

@Serializable
data class Article(
    val id: Int,
    val title: String,
    val link: String,
    val author: String? = null,
    val shareUser: String? = null,
    val chapterName: String? = null,
    val superChapterName: String? = null,
    val desc: String? = null,
    val envelopePic: String? = null,
    val niceDate: String? = null,
    val niceShareDate: String? = null,
    val collect: Boolean = false,
    val type: Int = 0,
) {
    val displayAuthor get() = author?.takeIf { it.isNotBlank() } ?: shareUser.orEmpty()
    val displayCategory get() = listOfNotNull(superChapterName, chapterName)
        .filter { it.isNotBlank() }.distinct().joinToString("/").plainText()
    val displayTime get() = niceDate?.takeIf { it.isNotBlank() } ?: niceShareDate.orEmpty()
}

@Serializable
data class ArticlePage(
    val datas: List<Article> = emptyList(),
    val curPage: Int = 0,
    val pageCount: Int = 0,
    val over: Boolean = false,
)

@Serializable
data class Chapter(val id: Int, val name: String = "", val children: List<Chapter> = emptyList())

fun String.plainText(): String = replace(Regex("(?i)<br\\s*/?>"), "\n")
    .replace(Regex("<[^>]*>"), "").decodeHtmlEntities().trim()

enum class HomeTab(val title: String, val firstPage: Int, val hasChapters: Boolean = false) {
    Hot("热门", 0), Square("广场", 0), Project("项目", 1, true), WeChat("公众号", 1, true)
}

data class FeedKey(val tab: HomeTab, val chapterId: Int? = null)
data class FeedState(
    val articles: List<Article> = emptyList(),
    val initialized: Boolean = false,
    val refreshing: Boolean = false,
    val loadingMore: Boolean = false,
    val nextPage: Int = 0,
    val endReached: Boolean = false,
    val error: String? = null,
    val refreshFailed: Boolean = false,
)
data class ChaptersState(
    val items: List<Chapter> = emptyList(),
    val loading: Boolean = false,
    val loaded: Boolean = false,
    val error: String? = null,
)
data class HomeState(
    val selectedTab: HomeTab = HomeTab.Hot,
    val selectedChapters: Map<HomeTab, Int> = emptyMap(),
    val chapters: Map<HomeTab, ChaptersState> = emptyMap(),
    val feeds: Map<FeedKey, FeedState> = emptyMap(),
    val collecting: Set<Int> = emptySet(),
)

sealed interface HomeIntent {
    data class SelectTab(val tab: HomeTab) : HomeIntent
    data class SelectChapter(val tab: HomeTab, val id: Int) : HomeIntent
    data class EnsureTab(val tab: HomeTab) : HomeIntent
    data class Refresh(val key: FeedKey) : HomeIntent
    data class LoadMore(val key: FeedKey) : HomeIntent
    data class RetryChapters(val tab: HomeTab) : HomeIntent
    data class OpenArticle(val article: Article) : HomeIntent
    data class ToggleCollect(val articleId: Int) : HomeIntent
    data class SessionChanged(val loggedIn: Boolean) : HomeIntent
    data class CollectionChanged(val articleId: Int, val collected: Boolean) : HomeIntent
    object Search : HomeIntent
}
sealed interface HomeEffect {
    object OpenSearch : HomeEffect
    object RequestLogin : HomeEffect
    data class OpenArticle(val article: Article) : HomeEffect
    data class Message(val text: String) : HomeEffect
}
