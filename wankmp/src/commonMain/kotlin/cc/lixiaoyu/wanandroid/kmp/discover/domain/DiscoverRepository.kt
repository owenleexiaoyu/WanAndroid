package cc.lixiaoyu.wanandroid.kmp.discover.domain

import cc.lixiaoyu.wanandroid.kmp.discover.data.remote.DiscoverRemoteDataSource
import cc.lixiaoyu.wanandroid.kmp.nav.util.decodeHtmlEntities
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class DiscoverRepository(
    private val remoteDataSource: DiscoverRemoteDataSource,
) {
    suspend fun loadDiscover(): Result<DiscoverContent> = runCatching {
        coroutineScope {
            val banners = async { remoteDataSource.fetchBanners() }
            val routes = async { remoteDataSource.fetchPopularRoutes() }
            val wenda = async { remoteDataSource.fetchPopularWenda() }
            val columns = async { remoteDataSource.fetchPopularColumns() }

            DiscoverContent(
                banners = banners.await().map {
                    DiscoverBanner(
                        id = it.id,
                        title = it.title.decodeHtmlEntities(),
                        imageUrl = it.imagePath,
                        link = it.url,
                    )
                },
                sections = listOf(
                    PopularSection(
                        type = PopularSectionType.Route,
                        title = "最新学习路线",
                        moreLabel = "更多",
                        items = routes.await().map {
                            PopularItem(
                                id = it.id,
                                title = it.name.decodeHtmlEntities(),
                                link = "https://www.wanandroid.com/route/${it.id}",
                            )
                        },
                    ),
                    PopularSection(
                        type = PopularSectionType.Wenda,
                        title = "最受欢迎问答",
                        moreLabel = "更多",
                        items = wenda.await().map {
                            PopularItem(
                                id = it.id,
                                title = it.title.decodeHtmlEntities(),
                                link = it.link,
                            )
                        },
                    ),
                    PopularSection(
                        type = PopularSectionType.Column,
                        title = "最受欢迎专栏",
                        moreLabel = "更多",
                        items = columns.await().map {
                            PopularItem(
                                id = it.id,
                                title = it.name.decodeHtmlEntities(),
                                link = it.url.ifBlank { "https://www.wanandroid.com/column/${it.id}" },
                            )
                        },
                    ),
                ),
            )
        }
    }
}

data class DiscoverContent(
    val banners: List<DiscoverBanner>,
    val sections: List<PopularSection>,
)
