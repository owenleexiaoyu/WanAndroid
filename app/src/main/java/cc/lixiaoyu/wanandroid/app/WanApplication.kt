package cc.lixiaoyu.wanandroid.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import cc.lixiaoyu.wanandroid.BuildConfig
import com.tencent.bugly.crashreport.CrashReport
import org.litepal.LitePal

class WanApplication : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        globalContext = applicationContext
        initLitepal()
        initBugly()
    }

    /**
     * 初始化 bugly
     */
    private fun initBugly() {
        CrashReport.initCrashReport(this, "7e1141d5de", BuildConfig.DEBUG)
    }

    /**
     * 初始化Litepal数据库
     */
    private fun initLitepal() {
        LitePal.initialize(this)
    }

    companion object {
        /**
         * 获取全局Context的方法，便于在任何位置获取到Context
         * @return
         */
        var globalContext: Context? = null
            private set
    }
}