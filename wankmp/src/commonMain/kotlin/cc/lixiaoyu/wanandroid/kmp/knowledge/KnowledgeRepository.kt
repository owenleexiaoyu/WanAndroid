package cc.lixiaoyu.wanandroid.kmp.knowledge

import cc.lixiaoyu.wanandroid.kmp.home.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface KnowledgeRepository {
    suspend fun tree(): List<Chapter>
    suspend fun articles(id: Int, page: Int): ArticlePage
    suspend fun collect(id: Int, collected: Boolean)
}

class KtorKnowledgeRepository(private val client: HttpClient) : KnowledgeRepository {
    private val home = KtorHomeRepository(client)
    override suspend fun tree(): List<Chapter> = unwrap(
        client.get("${KtorHomeRepository.BASE}/tree/json").body<WanResponse<List<Chapter>>>()
    ).distinctBy { it.id }.map { it.copy(children = it.children.distinctBy { child -> child.id }) }
        .filter { it.children.isNotEmpty() }

    override suspend fun articles(id: Int, page: Int): ArticlePage = unwrap(
        client.get("${KtorHomeRepository.BASE}/article/list/$page/json") { parameter("cid", id) }
            .body<WanResponse<ArticlePage>>()
    )
    override suspend fun collect(id: Int, collected: Boolean) = home.collect(id, collected)
    private fun <T> unwrap(response: WanResponse<T>): T {
        if (response.errorCode != 0) throw WanApiException(response.errorCode, response.errorMsg.ifBlank { "请求失败" })
        return response.data ?: error("返回数据为空")
    }
}
