package cc.lixiaoyu.wanandroid.util;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.app.MyApplication;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 使用Retrofit的封装类
 */
public class RetrofitHelper {
    private static ClearableCookieJar cookieJar;
    private static OkHttpClient okHttpClient;
    private Retrofit mRetrofit;

    private RetrofitHelper(){
        initRetrofit();
    }

    /**
     * 初始化Retrofit
     */
    private void initRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(AppConst.WANANDROID_BASEURL)
                .callFactory(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 获得Service接口
     * @return
     */
    public WanAndroidService getWanAndroidService(){
        return mRetrofit.create(WanAndroidService.class);
    }

    public static RetrofitHelper getInstance(){
        return SingleTonHolder.INSTANCE;
    }

    private static OkHttpClient getOkHttpClient(){
        cookieJar= new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(MyApplication.getGlobalContext()));
        okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
        return okHttpClient;
    }

    //实现单例模式的静态内部类
    private static class SingleTonHolder{
        private static final RetrofitHelper INSTANCE = new RetrofitHelper();
    }
}




