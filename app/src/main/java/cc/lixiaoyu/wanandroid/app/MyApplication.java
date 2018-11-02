package cc.lixiaoyu.wanandroid.app;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

public class MyApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initLitepal();
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
