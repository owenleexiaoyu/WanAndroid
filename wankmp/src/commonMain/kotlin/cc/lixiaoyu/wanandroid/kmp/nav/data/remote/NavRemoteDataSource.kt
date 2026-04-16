package cc.lixiaoyu.wanandroid.kmp.nav.data.remote

import cc.lixiaoyu.wanandroid.kmp.nav.data.dto.Nav
import cc.lixiaoyu.wanandroid.kmp.nav.data.dto.WanNavResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/**
 * Loads navigation categories from WanAndroid `navi/json`.
 * 网络模拟请在 [HttpClient] 层使用 [io.ktor.client.engine.mock.MockEngine]（见 [cc.lixiaoyu.wanandroid.kmp.nav.testing.createNavHttpClient]），避免侵入此处业务逻辑。
 */
class NavRemoteDataSource(
    private val client: HttpClient,
) {
    suspend fun fetchNavigation(): Result<List<Nav>> = runCatching {
        val response: WanNavResponse = client.get(NAV_JSON_URL).body()
        if (response.errorCode != 0) {
            val msg = response.errorMsg.ifBlank { "errorCode=${response.errorCode}" }
            throw IllegalStateException(msg)
        }
        response.data ?: emptyList()
    }

    companion object {
        const val NAV_JSON_URL = "https://wanandroid.com/navi/json"
    }
}
