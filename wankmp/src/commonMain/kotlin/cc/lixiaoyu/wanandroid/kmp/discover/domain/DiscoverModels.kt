package cc.lixiaoyu.wanandroid.kmp.discover.domain

data class DiscoverBanner(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val link: String,
)

data class PopularSection(
    val type: PopularSectionType,
    val title: String,
    val moreLabel: String,
    val items: List<PopularItem>,
)

data class PopularItem(
    val id: Int,
    val title: String,
    val link: String,
)

enum class PopularSectionType {
    Route,
    Wenda,
    Column,
}
