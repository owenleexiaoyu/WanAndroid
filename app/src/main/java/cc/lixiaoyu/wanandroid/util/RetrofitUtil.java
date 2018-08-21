package cc.lixiaoyu.wanandroid.util;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

    private static Retrofit mRetrofit;
    private static  WanAndroidService mService;
    public static WanAndroidService getWanAndroidService(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl(AppConst.WANANDROID_BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = mRetrofit.create(WanAndroidService.class);
        return mService;
    }

    class WanAndroidCallback<T> implements Callback<T> {

        @Override
        public void onResponse(Call<T> call, Response<T> response) {

        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {

        }
    }
}
