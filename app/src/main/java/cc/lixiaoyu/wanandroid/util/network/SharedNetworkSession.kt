package cc.lixiaoyu.wanandroid.util.network

import cc.lixiaoyu.wanandroid.app.WanApplication
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor

/** One in-memory and persisted cookie store for Retrofit and Ktor. */
object SharedNetworkSession {
    val cookieJar: ClearableCookieJar by lazy {
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(requireNotNull(WanApplication.globalContext)))
    }
}
