package cc.lixiaoyu.wanandroid.util.network.interceptor;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.HashSet;

import cc.lixiaoyu.wanandroid.app.WanApplication;
import okhttp3.Interceptor;
import okhttp3.Response;

public class SaveCookiesInterceptor implements Interceptor {
    private static final String COOKIE_PREF = "cookies_prefs";
    private static final String COOKIE_KEY = "cookies_key";

    private Context mContext = WanApplication.Companion.getGlobalContext();
    private SharedPreferences sp;
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            sp = mContext.getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
            sp.edit().putStringSet(COOKIE_KEY, cookies).apply();
        }

        return originalResponse;
    }
}
