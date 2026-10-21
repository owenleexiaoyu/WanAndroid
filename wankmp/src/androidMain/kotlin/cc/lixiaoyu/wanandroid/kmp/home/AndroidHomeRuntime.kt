package cc.lixiaoyu.wanandroid.kmp.home

import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.createDefaultJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import okhttp3.CookieJar

/** Shares authentication storage with the legacy client, but owns its engine. */
class AndroidHomeRuntime(cookieJar: CookieJar, scope: CoroutineScope, loggedIn: Boolean) {
    private val client = HttpClient(OkHttp) {
        engine { config { cookieJar(cookieJar) } }
        install(ContentNegotiation) { json(createDefaultJson()) }
    }
    val viewModel = HomeViewModel(KtorHomeRepository(client), scope, loggedIn)
    fun close() { viewModel.close(); client.close() }
}
