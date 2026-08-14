package cc.lixiaoyu.wanandroid.kmp.discover.mvi

import cc.lixiaoyu.wanandroid.kmp.discover.domain.DiscoverBanner
import cc.lixiaoyu.wanandroid.kmp.discover.domain.PopularItem
import cc.lixiaoyu.wanandroid.kmp.discover.domain.PopularSectionType

sealed interface DiscoverIntent {
    data object Enter : DiscoverIntent
    data object Retry : DiscoverIntent
    data class BannerPageChanged(val index: Int) : DiscoverIntent
    data class ClickBanner(val banner: DiscoverBanner) : DiscoverIntent
    data class ClickPopularItem(val item: PopularItem) : DiscoverIntent
    data class ClickMore(val type: PopularSectionType) : DiscoverIntent
}
