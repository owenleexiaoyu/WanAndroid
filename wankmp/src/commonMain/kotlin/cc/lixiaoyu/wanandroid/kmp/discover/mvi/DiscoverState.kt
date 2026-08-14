package cc.lixiaoyu.wanandroid.kmp.discover.mvi

import cc.lixiaoyu.wanandroid.kmp.discover.domain.DiscoverBanner
import cc.lixiaoyu.wanandroid.kmp.discover.domain.PopularSection

data class DiscoverState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val banners: List<DiscoverBanner> = emptyList(),
    val currentBannerIndex: Int = 0,
    val sections: List<PopularSection> = emptyList(),
)
