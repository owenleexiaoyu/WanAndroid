package cc.lixiaoyu.wanandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.HashSet;

import cc.lixiaoyu.wanandroid.app.MyApplication;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {
    private static final String COOKIE_PREF = "cookies_prefs";
    private static final String COOKIE_KEY = "cookies_key";
    private Context mContext = MyApplication.getGlobalContext();
    private SharedPreferences sp;
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        sp = mContext.getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
        HashSet<String> preferences = (HashSet) sp.getStringSet(COOKIE_KEY, new HashSet<String>());
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
        }

        return chain.proceed(builder.build());
    }
}
