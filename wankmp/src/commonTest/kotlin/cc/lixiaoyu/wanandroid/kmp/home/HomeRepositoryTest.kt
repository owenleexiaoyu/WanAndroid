package cc.lixiaoyu.wanandroid.kmp.home

import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.createDefaultJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class HomeRepositoryTest {
    @Test fun pathsPagingModelsAndCollection() = runTest {
        val paths = mutableListOf<String>()
        val engine = MockEngine { request ->
            paths += request.url.encodedPath
            assertEquals("wanandroid.com", request.url.host)
            if (request.url.encodedPath.startsWith("/project/list")) assertEquals("42", request.url.parameters["cid"])
            val body = when {
                request.method == HttpMethod.Post -> """{"data":null,"errorCode":0}"""
                request.url.encodedPath.contains("chapters") || request.url.encodedPath.contains("tree") ->
                    """{"data":[{"id":42,"name":"Kotlin","extra":true}]}"""
                request.url.encodedPath.contains("/top/") -> """{"data":[{"id":7,"title":"top","link":"https://example.com"}]}"""
                else -> """{"data":{"datas":[{"id":7,"title":"A &amp; B","link":"https://example.com","author":null,"shareUser":"分享人","desc":null,"chapterName":null}],"curPage":1,"pageCount":3,"over":false}}"""
            }
            respond(body, headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }
        val client = HttpClient(engine) { install(ContentNegotiation) { json(createDefaultJson()) } }
        try {
            val repository = KtorHomeRepository(client)
            assertEquals("分享人", repository.articles(FeedKey(HomeTab.Hot), 0).datas.first().displayAuthor)
            repository.articles(FeedKey(HomeTab.Square), 0)
            repository.articles(FeedKey(HomeTab.Project, 42), 1)
            repository.articles(FeedKey(HomeTab.WeChat, 42), 1)
            assertEquals(42, repository.chapters(HomeTab.Project).first().id)
            repository.chapters(HomeTab.WeChat)
            assertEquals(1, repository.topArticles().first().type)
            repository.collect(7, true)
            repository.collect(7, false)
            assertEquals(listOf("/article/list/0/json", "/user_article/list/0/json", "/project/list/1/json",
                "/wxarticle/list/42/1/json", "/project/tree/json", "/wxarticle/chapters/json", "/article/top/json",
                "/lg/collect/7/json", "/lg/uncollect_originId/7/json"), paths)
            assertEquals("—“引用”…", "&mdash;&ldquo;引用&rdquo;&hellip;".plainText())
            assertEquals("A & B\n描述", "<b>A &amp; B</b><br/>描述".plainText())
        } finally { client.close() }
    }
    @Test fun businessFailureAndEmptyResponse() = runTest {
        var body = """{"errorCode":-1001,"errorMsg":"请登录"}"""
        val client = HttpClient(MockEngine { respond(body, headers = headersOf(HttpHeaders.ContentType, "application/json")) }) {
            install(ContentNegotiation) { json(createDefaultJson()) }
        }
        try {
            val repository = KtorHomeRepository(client)
            assertEquals(-1001, assertFailsWith<WanApiException> { repository.collect(1, true) }.code)
            body = """{"errorCode":0,"data":null}"""
            assertFailsWith<IllegalStateException> { repository.articles(FeedKey(HomeTab.Hot), 0) }
        } finally { client.close() }
    }
}
