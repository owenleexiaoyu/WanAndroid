package cc.lixiaoyu.wanandroid.kmp.discover.android

import android.content.Context

fun interface DiscoverWebDetailOpener {
    fun openDiscoverWebDetail(context: Context, title: String, url: String)
}
