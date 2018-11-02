package cc.lixiaoyu.wanandroid.util;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

    private static Retrofit mRetrofit;
    private static  WanAndroidService mService;
    public static WanAndroidService getWanAndroidService(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl(AppConst.WANANDROID_BASEURL)
                .callFactory(OkhttpUtil.getInstance().getOkHttpClientWithCookieJar())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = mRetrofit.create(WanAndroidService.class);
        return mService;
    }
}
