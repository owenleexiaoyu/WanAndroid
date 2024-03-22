package cc.lixiaoyu.wanandroid.core.search.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import cc.lixiaoyu.wanandroid.api.WanAndroidService
import cc.lixiaoyu.wanandroid.entity.Article
import cc.lixiaoyu.wanandroid.util.ToastUtil
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchResultViewModel(val keyword: String): ViewModel() {
    // 分页页码
    private var currentPage: Int = 0
    private val apiService: WanAndroidService = RetrofitManager.wanAndroidService

    private val _searchResultList: MutableStateFlow<List<Article>> = MutableStateFlow(
        emptyList()
    )
    val searchResultList: StateFlow<List<Article>> = _searchResultList

    init {
        refreshSearchResult()
    }

    fun refreshSearchResult() {
        currentPage = 0
        viewModelScope.launch {
            try {
                val pageData = apiService.searchArticle(currentPage, keyword).data
                _searchResultList.value = pageData?.dataList ?: emptyList()
            } catch (t: Throwable) {
                t.printStackTrace()
                ToastUtil.showToast("出错了，请稍后再试~")
            }
        }
    }

    fun loadMoreSearchResult() {
        currentPage++
        viewModelScope.launch {
            try {
                val pageData = apiService.searchArticle(currentPage, keyword).data
                val articleList = pageData?.dataList ?: emptyList()
                if (articleList.isNotEmpty()) {
                    val newList = _searchResultList.value.toMutableList().apply {
                        addAll(articleList)
                    }
                    _searchResultList.value = newList
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                currentPage--
            }
        }
    }
}

class SearchResultViewModelFactory(private val keyword: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return SearchResultViewModel(keyword) as T
    }
}