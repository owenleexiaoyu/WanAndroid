package cc.lixiaoyu.wanandroid.kmp.nav.android

import android.content.Context

/**
 * 由嵌入 [NavContainerFragment] 的 Activity 实现，用于打开应用内网页详情（避免 wankmp 依赖 app 模块）。
 */
fun interface NavWebDetailOpener {
    fun openNavWebDetail(context: Context, title: String, url: String)
}
