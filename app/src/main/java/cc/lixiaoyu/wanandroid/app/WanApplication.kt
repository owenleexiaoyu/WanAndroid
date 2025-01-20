package cc.lixiaoyu.wanandroid.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import cc.lixiaoyu.wanandroid.BuildConfig
import cc.lixiaoyu.wanandroid.core.account.AccountManager
import cc.lixiaoyu.wanandroid.core.theme.ThemeManager
import com.tencent.bugly.crashreport.CrashReport

class WanApplication : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        globalContext = applicationContext
        initBugly()
        initAccount()
        initTheme()
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

    private fun initTheme() {
        if (ThemeManager.isDarkMode.value) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
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