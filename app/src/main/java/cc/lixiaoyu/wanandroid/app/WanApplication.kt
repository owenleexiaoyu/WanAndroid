package cc.lixiaoyu.wanandroid.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import cc.lixiaoyu.wanandroid.BuildConfig
import cc.lixiaoyu.wanandroid.core.account.AccountManager
import com.tencent.bugly.crashreport.CrashReport
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
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
        initAccount()
    }

    @SuppressLint("CheckResult")
    private fun initAccount() {
        val isLogin = AccountManager.isLogin()
        Log.d("OWEN", "initAccount, isLogin = $isLogin")
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