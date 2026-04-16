package cc.lixiaoyu.wanandroid.kmp.nav.testing

import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.createDefaultJson
import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.createNavigationHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay

/**
 * 根据 [scenario] 创建 [HttpClient]：
 * - [NavNetworkMockScenario.None]：平台真实 Engine（OkHttp / Darwin）
 * - 其它：仅 [MockEngine]，在引擎层模拟弱网 / 断连 / 业务错误，**不侵入**
 *   [cc.lixiaoyu.wanandroid.kmp.nav.data.remote.NavRemoteDataSource]
 */
fun createNavHttpClient(scenario: NavNetworkMockScenario): HttpClient = when (scenario) {
    NavNetworkMockScenario.None -> createNavigationHttpClient()
    is NavNetworkMockScenario.WeakNetworkSuccess -> HttpClient(MockEngine) {
        install(ContentNegotiation) { json(createDefaultJson()) }
        engine {
            addHandler {
                delay(scenario.delayMillis)
                respond(
                    content = MOCK_NAV_JSON_SUCCESS,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
        }
    }
    is NavNetworkMockScenario.WeakNetworkFailure -> HttpClient(MockEngine) {
        install(ContentNegotiation) { json(createDefaultJson()) }
        engine {
            addHandler {
                delay(scenario.delayMillis)
                throw Exception(scenario.message)
            }
        }
    }
    is NavNetworkMockScenario.ConnectionFailure -> HttpClient(MockEngine) {
        install(ContentNegotiation) { json(createDefaultJson()) }
        engine {
            addHandler {
                delay(scenario.delayMillis)
                throw Exception(scenario.message)
            }
        }
    }
    is NavNetworkMockScenario.ApiBusinessFailure -> HttpClient(MockEngine) {
        install(ContentNegotiation) { json(createDefaultJson()) }
        engine {
            addHandler {
                val safe = scenario.errorMsg
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                val body = """{"data":null,"errorCode":${scenario.errorCode},"errorMsg":"$safe"}"""
                respond(
                    content = body,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
        }
    }
}

/** 与 [cc.lixiaoyu.wanandroid.kmp.nav.data.remote.NavRemoteDataSource.NAV_JSON_URL] 请求对应的 mock 成功体。 */
private val MOCK_NAV_JSON_SUCCESS = """
{"data":[{"articles":[{"id":1,"title":"Mock站点","link":"https://example.com"}],"name":"Mock分类"}],"errorCode":0,"errorMsg":""}
""".trimIndent()
