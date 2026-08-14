package cc.lixiaoyu.wanandroid.kmp.discover.data.remote

import cc.lixiaoyu.wanandroid.kmp.discover.data.dto.DiscoverBannerDto
import cc.lixiaoyu.wanandroid.kmp.discover.data.dto.PopularColumnDto
import cc.lixiaoyu.wanandroid.kmp.discover.data.dto.PopularRouteDto
import cc.lixiaoyu.wanandroid.kmp.discover.data.dto.PopularWendaDto
import cc.lixiaoyu.wanandroid.kmp.discover.data.dto.WanDiscoverResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class DiscoverRemoteDataSource(
    private val client: HttpClient,
) {
    suspend fun fetchBanners(): List<DiscoverBannerDto> {
        return unwrap(client.get(BANNER_URL).body<WanDiscoverResponse<List<DiscoverBannerDto>>>())
    }

    suspend fun fetchPopularWenda(): List<PopularWendaDto> {
        return unwrap(client.get(POPULAR_WENDA_URL).body<WanDiscoverResponse<List<PopularWendaDto>>>())
    }

    suspend fun fetchPopularRoutes(): List<PopularRouteDto> {
        return unwrap(client.get(POPULAR_ROUTE_URL).body<WanDiscoverResponse<List<PopularRouteDto>>>())
    }

    suspend fun fetchPopularColumns(): List<PopularColumnDto> {
        return unwrap(client.get(POPULAR_COLUMN_URL).body<WanDiscoverResponse<List<PopularColumnDto>>>())
    }

    private fun <T> unwrap(response: WanDiscoverResponse<T>): T {
        if (response.errorCode != 0) {
            val msg = response.errorMsg.ifBlank { "errorCode=${response.errorCode}" }
            throw IllegalStateException(msg)
        }
        return response.data ?: throw IllegalStateException("empty response")
    }

    companion object {
        const val BANNER_URL = "https://wanandroid.com/banner/json"
        const val POPULAR_WENDA_URL = "https://wanandroid.com/popular/wenda/json"
        const val POPULAR_ROUTE_URL = "https://wanandroid.com/popular/route/json"
        const val POPULAR_COLUMN_URL = "https://wanandroid.com/popular/column/json"
    }
}
