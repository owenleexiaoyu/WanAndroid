package cc.lixiaoyu.wanandroid.core.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.lixiaoyu.wanandroid.api.WanAndroidService
import cc.lixiaoyu.wanandroid.core.search.model.HotKey
import cc.lixiaoyu.wanandroid.core.search.model.WebSite
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager
import cc.lixiaoyu.wanandroid.util.storage.DataManager
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val apiService: WanAndroidService by lazy { RetrofitManager.getInstance().wanAndroidService }
    private val dataManager: DataManager by lazy { DataManager() }

    private val _hotKeyList: MutableStateFlow<List<HotKey>> = MutableStateFlow(
        emptyList()
    )
    val hotKeyList: StateFlow<List<HotKey>> = _hotKeyList

    private val _commonSiteList: MutableStateFlow<List<WebSite>> = MutableStateFlow(
        emptyList()
    )
    val commonSiteList: StateFlow<List<WebSite>> = _commonSiteList

    private val _searchHistoryList: MutableStateFlow<List<String>> = MutableStateFlow(
        emptyList()
    )
    val searchHistoryList: StateFlow<List<String>> = _searchHistoryList

    init {
        getHotKeyListAndCommonSiteList()
        getSearchHistoryList()
    }

    private fun getHotKeyListAndCommonSiteList() {
        viewModelScope.launch {
            try {
                val hotKeyDeferred = async { apiService.getHotKeyList() }
                val commonSiteDeferred = async { apiService.getCommonSiteList() }
                hotKeyDeferred.await().data?.let {
                    _hotKeyList.value = it
                }
                commonSiteDeferred.await().data?.let {
                    _commonSiteList.value = it
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    private fun getSearchHistoryList() {
        viewModelScope.launch {
            val historyList = dataManager.loadAllHistoryData()
            _searchHistoryList.value = historyList
        }
    }

    fun searchKeyword(keyword: String) {
        dataManager.addHistoryData(keyword)
        _searchHistoryList.value = _searchHistoryList.value.toMutableList().apply {
            add(keyword)
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            dataManager.clearAllHistoryData()
            _searchHistoryList.value = emptyList()
        }
    }
}