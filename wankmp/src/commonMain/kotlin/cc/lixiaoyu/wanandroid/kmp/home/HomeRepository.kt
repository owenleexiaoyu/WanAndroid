package cc.lixiaoyu.wanandroid.kmp.home

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post

class WanApiException(val code: Int, message: String) : Exception(message)

interface HomeRepository {
    suspend fun chapters(tab: HomeTab): List<Chapter>
    suspend fun articles(key: FeedKey, page: Int): ArticlePage
    suspend fun topArticles(): List<Article>
    suspend fun collect(id: Int, collected: Boolean)
}

class KtorHomeRepository(private val client: HttpClient) : HomeRepository {
    override suspend fun chapters(tab: HomeTab): List<Chapter> = unwrap(
        client.get("$BASE/${if (tab == HomeTab.Project) "project/tree" else "wxarticle/chapters"}/json")
            .body<WanResponse<List<Chapter>>>()
    )

    override suspend fun articles(key: FeedKey, page: Int): ArticlePage {
        val path = when (key.tab) {
            HomeTab.Hot -> "article/list/$page/json"
            HomeTab.Square -> "user_article/list/$page/json"
            HomeTab.Project -> "project/list/$page/json"
            HomeTab.WeChat -> "wxarticle/list/${requireNotNull(key.chapterId)}/$page/json"
        }
        return unwrap(client.get("$BASE/$path") {
            if (key.tab == HomeTab.Project) parameter("cid", requireNotNull(key.chapterId))
        }.body<WanResponse<ArticlePage>>())
    }

    override suspend fun topArticles(): List<Article> = unwrap(
        client.get("$BASE/article/top/json").body<WanResponse<List<Article>>>()
    ).map { it.copy(type = 1) }

    override suspend fun collect(id: Int, collected: Boolean) {
        val path = if (collected) "collect" else "uncollect_originId"
        val response = client.post("$BASE/lg/$path/$id/json").body<WanResponse<kotlinx.serialization.json.JsonElement>>()
        checkResponse(response)
    }

    private fun checkResponse(response: WanResponse<*>) {
        if (response.errorCode != 0) throw WanApiException(response.errorCode, response.errorMsg.ifBlank { "请求失败" })
    }
    private fun <T> unwrap(response: WanResponse<T>): T {
        checkResponse(response)
        return response.data ?: throw IllegalStateException("返回数据为空")
    }
    companion object { const val BASE = "https://wanandroid.com" }
}
