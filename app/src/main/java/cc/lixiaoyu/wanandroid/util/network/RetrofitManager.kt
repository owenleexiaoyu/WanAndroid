package cc.lixiaoyu.wanandroid.util.network

import cc.lixiaoyu.wanandroid.api.WanAndroidService
import cc.lixiaoyu.wanandroid.util.AppConst
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 使用Retrofit的封装类
 */
object RetrofitManager  {

    private lateinit var retrofit: Retrofit

    init {
        initRetrofit()
    }

    /**
     * 初始化Retrofit
     */
    private fun initRetrofit() {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .cookieJar(SharedNetworkSession.cookieJar)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(AppConst.WANANDROID_BASEURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    /**
     * 获得Service接口
     * @return
     */
    val wanAndroidService: WanAndroidService
        get() = retrofit.create(WanAndroidService::class.java)

}