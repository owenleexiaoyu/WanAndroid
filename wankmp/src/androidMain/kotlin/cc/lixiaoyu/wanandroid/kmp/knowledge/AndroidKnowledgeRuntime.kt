package cc.lixiaoyu.wanandroid.kmp.knowledge

import cc.lixiaoyu.wanandroid.kmp.home.createAuthenticatedHomeHttpClient
import kotlinx.coroutines.CoroutineScope
import okhttp3.CookieJar

class AndroidKnowledgeRuntime(cookieJar: CookieJar, scope: CoroutineScope, loggedIn: Boolean) {
    private val client = createAuthenticatedHomeHttpClient(cookieJar)
    val viewModel = KnowledgeViewModel(KtorKnowledgeRepository(client), scope, loggedIn)
    fun close() { viewModel.close(); client.close() }
}
