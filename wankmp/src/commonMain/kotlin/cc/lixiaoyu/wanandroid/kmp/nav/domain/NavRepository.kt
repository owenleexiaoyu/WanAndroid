package cc.lixiaoyu.wanandroid.kmp.nav.domain

import cc.lixiaoyu.wanandroid.kmp.nav.data.dto.Nav
import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.NavRemoteDataSource

class NavRepository(
    private val remote: NavRemoteDataSource,
) {
    suspend fun loadNavigation(): Result<List<Nav>> = remote.fetchNavigation()
}

