package cc.lixiaoyu.wanandroid.util.network

import cc.lixiaoyu.wanandroid.api.WanAndroidService
import cc.lixiaoyu.wanandroid.app.WanApplication.Companion.globalContext
import cc.lixiaoyu.wanandroid.util.AppConst
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
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
        val cookieJar: ClearableCookieJar = PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(globalContext)
        )
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .cookieJar(cookieJar)
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