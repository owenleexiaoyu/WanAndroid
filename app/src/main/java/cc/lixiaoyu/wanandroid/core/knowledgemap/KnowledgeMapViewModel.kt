package cc.lixiaoyu.wanandroid.core.knowledgemap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.lixiaoyu.wanandroid.core.account.AccountManager
import cc.lixiaoyu.wanandroid.kmp.home.HomeCollectionEvents
import cc.lixiaoyu.wanandroid.kmp.knowledge.*
import cc.lixiaoyu.wanandroid.util.network.SharedNetworkSession
import kotlinx.coroutines.launch

class KnowledgeMapViewModel : ViewModel() {
    private val runtime = AndroidKnowledgeRuntime(SharedNetworkSession.cookieJar, viewModelScope, AccountManager.isLogin())
    val shared = runtime.viewModel
    init {
        viewModelScope.launch {
            HomeCollectionEvents.changes.collect { (id, collected) ->
                shared.dispatch(KnowledgeIntent.CollectionChanged(id, collected))
            }
        }
    }
    override fun onCleared() { runtime.close() }
}
