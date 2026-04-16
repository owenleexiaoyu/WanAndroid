package cc.lixiaoyu.wanandroid.kmp.nav.mvi

import cc.lixiaoyu.wanandroid.kmp.nav.data.dto.Nav

data class NavState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val categories: List<Nav> = emptyList(),
) {
    val hasContent: Boolean get() = categories.isNotEmpty()
}

