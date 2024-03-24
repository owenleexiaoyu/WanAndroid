package cc.lixiaoyu.wanandroid.core.project.category

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

class CategoryProjectViewModel(private val projectId: String): ViewModel() {

    private val apiService: WanAndroidService by lazy { RetrofitManager.wanAndroidService }

    private var currentPage = 0

    private val _articleList: MutableStateFlow<List<Article>> = MutableStateFlow(listOf())
    val articleList: StateFlow<List<Article>> = _articleList

    private val _isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshing = _isRefreshing

    private val _isLoadingMore: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoadingMore = _isLoadingMore


    init {
        refreshCategoryProjectList()
    }

    fun refreshCategoryProjectList() {
        currentPage = 0
        getCategoryProjectList(currentPage)
    }

    fun loadMoreCategoryProjectList() {
        currentPage++
        getCategoryProjectList(currentPage)
    }

    private fun getCategoryProjectList(page: Int) {
        if (page == 0) {
            _isRefreshing.value = true
        } else {
            _isLoadingMore.value = true
        }
        viewModelScope.launch {
            try {
                val pageData = apiService.getProjectArticlesByCidNew(currentPage, projectId).data ?: return@launch
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
                ToastUtil.showToast("获取 Project 数据失败")
            } finally {
                _isRefreshing.value = false
                _isLoadingMore.value = false
            }
        }
    }


    fun collectArticleInArticleList(position: Int, articleId: Int) {
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

    fun unCollectArticleInArticleList(position: Int, articleId: Int) {
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

class CategoryProjectViewModelFactory(private val projectId: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return CategoryProjectViewModel(projectId) as T
    }

}