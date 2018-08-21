package cc.lixiaoyu.wanandroid.app;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    /**
     * 获取全局Context的方法，便于在任何位置获取到Context
     * @return
     */
    public static Context getGlobalContext(){
        return mContext;
    }
}
