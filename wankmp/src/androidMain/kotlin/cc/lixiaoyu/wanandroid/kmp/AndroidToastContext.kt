package cc.lixiaoyu.wanandroid.kmp

import android.content.Context

/**
 * 在宿主 Activity（如 [cc.lixiaoyu.wanandroid.core.about.KMPActivity]）启动时调用 [init]，
 * 以便 common 侧 [showShortToast] 能拿到 [Application] 级 Context。
 */
object AndroidToastContext {
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    internal fun getOrNull(): Context? = appContext
}
