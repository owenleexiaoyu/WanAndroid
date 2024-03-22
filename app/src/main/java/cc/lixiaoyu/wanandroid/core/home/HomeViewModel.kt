package cc.lixiaoyu.wanandroid.core.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.lixiaoyu.wanandroid.api.WanAndroidService
import cc.lixiaoyu.wanandroid.core.collection.CollectAbility
import cc.lixiaoyu.wanandroid.entity.Article
import cc.lixiaoyu.wanandroid.entity.Banner
import cc.lixiaoyu.wanandroid.util.ToastUtil
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val apiService: WanAndroidService by lazy { RetrofitManager.wanAndroidService }

    private var currentPage = 0

    private val _articleList: MutableStateFlow<List<Article>> = MutableStateFlow(listOf())
    val articleList: StateFlow<List<Article>> = _articleList

    private val _bannerList: MutableStateFlow<List<Banner>> = MutableStateFlow(listOf())
    val bannerList: StateFlow<List<Banner>> = _bannerList

    private val _isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshing = _isRefreshing

    private val _isLoadingMore: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoadingMore = _isLoadingMore


    init {
        getBanner()
        refreshHomeArticleList()
    }

    private fun getBanner() {
        viewModelScope.launch {
            try {
                val bannerList = apiService.getBannerDataNew().data ?: return@launch
                _bannerList.value = bannerList
            } catch (t: Throwable) {
                ToastUtil.showToast("获取Banner数据失败")
            }
        }
    }

    fun refreshHomeArticleList() {
        viewModelScope.launch {
            val deferredGetTopArticleList = async {
                getTopArticleList()
            }
            currentPage = 0
            val deferredGetFirstPageArticleList = async {
                getArticleList(currentPage)
            }
            try {
                val topArticleList = deferredGetTopArticleList.await()
                val firstPageArticleList = deferredGetFirstPageArticleList.await()
                val newList = mutableListOf<Article>()
                newList.addAll(topArticleList)
                newList.addAll(firstPageArticleList)
                _articleList.value = newList
            } catch (t: Throwable) {
                ToastUtil.showToast("获取首页文章数据失败")
            }
        }
    }

    fun loadMoreArticleList() {
        if (_isLoadingMore.value) {
            return
        }
        _isLoadingMore.value = true
        currentPage++
        viewModelScope.launch {
            try {
                val articleList = getArticleList(currentPage)
                if (articleList.isNotEmpty()) {
                    val newList = _articleList.value.toMutableList()
                    newList.addAll(articleList)
                    _articleList.value = newList
                }
            } catch (t: Throwable) {
                ToastUtil.showToast("加载更多文章数据失败")
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    private suspend fun getTopArticleList(): List<Article> {
        return apiService.getTopArticlesNew().data ?: emptyList()
    }

    private suspend fun getArticleList(page: Int): List<Article> {
        return apiService.getArticleListNew(page, null).data?.dataList ?: emptyList()
    }

    fun collectOrUnCollectArticle(position: Int, article: Article) {
        if (article.isCollect) {
            CollectAbility.unCollectArticle(null, article.id) { success ->
                if (success) {
                    article.isCollect = false
                    _articleList.value = _articleList.value
                    ToastUtil.showToast("取消收藏成功")
                } else {
                    ToastUtil.showToast("取消收藏失败")
                }
            }
        } else {
            CollectAbility.collectArticle(null, article.id) { success ->
                if (success) {
                    article.isCollect = true
                    _articleList.value = _articleList.value
                    ToastUtil.showToast("收藏成功")
                } else {
                    ToastUtil.showToast("收藏失败")
                }
            }
        }
    }
}