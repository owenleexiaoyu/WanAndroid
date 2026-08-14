package cc.lixiaoyu.wanandroid.kmp.discover.mvi

sealed interface DiscoverEffect {
    data class OpenUrl(val title: String, val url: String) : DiscoverEffect
    data class ShowToast(val message: String) : DiscoverEffect
}
