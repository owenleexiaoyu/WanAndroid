package cc.lixiaoyu.wanandroid.kmp.nav.mvi

import cc.lixiaoyu.wanandroid.kmp.nav.data.dto.NavItem

sealed interface NavIntent {
    data object Enter : NavIntent
    data object Retry : NavIntent
    data class ClickItem(val item: NavItem) : NavIntent
}

