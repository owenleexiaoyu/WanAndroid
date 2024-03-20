package cc.lixiaoyu.wanandroid.core.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.lixiaoyu.wanandroid.api.WanAndroidService
import cc.lixiaoyu.wanandroid.entity.Article
import cc.lixiaoyu.wanandroid.util.ToastUtil
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CollectionViewModel: ViewModel() {

    private val apiService: WanAndroidService by lazy { RetrofitManager.getInstance().wanAndroidService }

    private var currentPage = 0

    private val _articleList: MutableStateFlow<List<Article>> = MutableStateFlow(listOf())
    val articleList: StateFlow<List<Article>> = _articleList

    private val _isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshing = _isRefreshing

    private val _isLoadingMore: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoadingMore = _isLoadingMore

    init {
        refreshCollectionArticleList()
    }

    fun refreshCollectionArticleList() {
        currentPage = 0
        _isRefreshing.value = true
        getCollectionArticleList(currentPage)
    }

    fun loadMoreCollectionArticleList() {
        currentPage++
        getCollectionArticleList(currentPage)
    }

    private fun getCollectionArticleList(page: Int) {
        viewModelScope.launch {
            try {
                val pageData = apiService.getCollectionArticleListNew(page).data ?: return@launch
                if (pageData.dataList.isNotEmpty()) {
                   if (page == 0) {
                       _articleList.value = pageData.dataList
                   } else {
                       val newList = _articleList.value.toMutableList().apply {
                           addAll(pageData.dataList)
                       }
                       _articleList.value = newList
                   }
                }
            } catch (t: Throwable) {
                ToastUtil.showToast("获取收藏列表失败")
            } finally {
                _isRefreshing.value = false
                _isLoadingMore.value = false
            }
        }
    }

    fun unCollectArticle(position: Int, aid: Int) {
        CollectAbility.unCollectArticle(null, aid) { success ->
            if (success) {
                val newList = _articleList.value.toMutableList().apply {
                    removeAt(position)
                }
                _articleList.value = newList
                ToastUtil.showToast("取消收藏成功")
            } else {
                ToastUtil.showToast("取消收藏失败")
            }
        }
//        viewModelScope.launch {
//            try {
//                apiService.unCollectArticleFromCollectionPageNew(aid, -1).data
//                val newList = _articleList.value.toMutableList().apply {
//                    removeAt(position)
//                }
//                _articleList.value = newList
//                ToastUtil.showToast("取消收藏成功")
//            } catch (t: Throwable) {
//                ToastUtil.showToast("取消收藏失败")
//            }
//        }
    }
}