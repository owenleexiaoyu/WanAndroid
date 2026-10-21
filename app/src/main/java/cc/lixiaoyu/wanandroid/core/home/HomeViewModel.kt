package cc.lixiaoyu.wanandroid.core.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.lixiaoyu.wanandroid.core.account.AccountManager
import cc.lixiaoyu.wanandroid.kmp.home.AndroidHomeRuntime
import cc.lixiaoyu.wanandroid.kmp.home.HomeCollectionEvents
import cc.lixiaoyu.wanandroid.kmp.home.HomeIntent
import cc.lixiaoyu.wanandroid.util.network.SharedNetworkSession
import kotlinx.coroutines.launch

/** Android lifecycle owner; all home business logic lives in commonMain. */
class HomeViewModel : ViewModel() {
    private val runtime = AndroidHomeRuntime(SharedNetworkSession.cookieJar, viewModelScope, AccountManager.isLogin())
    val shared = runtime.viewModel
    init {
        viewModelScope.launch {
            HomeCollectionEvents.changes.collect { (id, collected) ->
                shared.dispatch(HomeIntent.CollectionChanged(id, collected))
            }
        }
    }
    override fun onCleared() { runtime.close() }
}
