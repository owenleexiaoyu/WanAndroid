package cc.lixiaoyu.wanandroid.core.wechat.officialaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import cc.lixiaoyu.wanandroid.api.WanAndroidService
import cc.lixiaoyu.wanandroid.core.collection.CollectAbility
import cc.lixiaoyu.wanandroid.entity.Article
import cc.lixiaoyu.wanandroid.util.ToastUtil
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeChatOfficialAccountViewModel(private val officialAccountId: String): ViewModel() {

    private val apiService: WanAndroidService by lazy { RetrofitManager.wanAndroidService }

    private var currentPage = 1

    private val _articleList: MutableStateFlow<List<Article>> = MutableStateFlow(listOf())
    val articleList: StateFlow<List<Article>> = _articleList

    private val _isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshing = _isRefreshing

    private val _isLoadingMore: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoadingMore = _isLoadingMore

    init {
        refreshWeChatOfficialAccountArticleList()
    }

    fun refreshWeChatOfficialAccountArticleList() {
        currentPage = 1
        getWeChatOfficialAccountArticleList(currentPage)
    }

    fun loadMoreWeChatOfficialAccountArticleList() {
        currentPage++
        getWeChatOfficialAccountArticleList(currentPage)
    }

    private fun getWeChatOfficialAccountArticleList(page: Int) {
        if (page == 1) {
            _isRefreshing.value = true
        } else {
            _isLoadingMore.value = true
        }
        viewModelScope.launch {
            try {
                val pageData = apiService.getWechatPublicArticlesByIdNew(officialAccountId, currentPage).data ?: return@launch
                if (pageData.dataList.isNotEmpty()) {
                    if (page == 1) {
                        _articleList.value = pageData.dataList
                    } else {
                        val newList = _articleList.value.toMutableList().apply {
                            addAll(pageData.dataList)
                        }
                        _articleList.value = newList
                    }
                }
            } catch (t: Throwable) {
                ToastUtil.showToast("获取 Project 数据失败")
            } finally {
                _isRefreshing.value = false
                _isLoadingMore.value = false
            }
        }
    }

    fun collectOrUnCollectArticleInArticleList(position: Int, article: Article) {
        if (article.isCollect) {
            unCollectArticleInArticleList(position, article.id)
        } else {
            collectArticleInArticleList(position, article.id)
        }
    }

    private fun collectArticleInArticleList(position: Int, articleId: Int) {
        CollectAbility.collectArticle(null, articleId) { success ->
            if (success) {
                val article = _articleList.value[position]
                article.isCollect = true
                _articleList.value = _articleList.value
                ToastUtil.showToast("收藏成功")
            } else {
                ToastUtil.showToast("收藏失败")
            }
        }
    }

    private fun unCollectArticleInArticleList(position: Int, articleId: Int) {
        CollectAbility.unCollectArticle(null, articleId) { success ->
            if (success) {
                val article = _articleList.value[position]
                article.isCollect = false
                _articleList.value = _articleList.value
                ToastUtil.showToast("取消收藏成功")
            } else {
                ToastUtil.showToast("取消收藏失败")
            }
        }
    }
}

class WeChatOfficialAccountViewModelFactory(private val officialAccountId: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return WeChatOfficialAccountViewModel(officialAccountId) as T
    }

}