package cc.lixiaoyu.wanandroid.kmp.nav.mvi

sealed interface NavEffect {
    data class OpenUrl(val title: String, val url: String) : NavEffect
}

