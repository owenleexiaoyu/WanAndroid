package cc.lixiaoyu.wanandroid.app;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePal;

import cc.lixiaoyu.wanandroid.BuildConfig;

public class MyApplication extends Application {
    private static Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initLitepal();
        initBugly();
    }

    /**
     * 初始化 bugly
     */
    private void initBugly() {
        CrashReport.initCrashReport(this, "7e1141d5de", BuildConfig.DEBUG);
    }

    /**
     * 获取全局Context的方法，便于在任何位置获取到Context
     * @return
     */
    public static Context getGlobalContext(){
        return mContext;
    }

    /**
     * 初始化Litepal数据库
     */
    private void initLitepal(){
        LitePal.initialize(this);
    }
}
