package cc.lixiaoyu.wanandroid.util;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import cc.lixiaoyu.wanandroid.app.MyApplication;
import okhttp3.OkHttpClient;

public class OkhttpUtil {
    private ClearableCookieJar cookieJar;
    private OkHttpClient okHttpClient;

    private static class SingleTonHolder{
        private static final OkhttpUtil INSTANCE = new OkhttpUtil();
    }

    private OkhttpUtil(){
        cookieJar= new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(MyApplication.getGlobalContext()));
        okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
    }

    public OkHttpClient getOkHttpClientWithCookieJar(){
        return okHttpClient;
    }

    public static OkhttpUtil getInstance(){
        return SingleTonHolder.INSTANCE;
    }

}
