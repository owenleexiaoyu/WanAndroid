package cc.lixiaoyu.wanandroid.core.knowledgemap.node

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.core.collection.CollectAbility
import cc.lixiaoyu.wanandroid.entity.Article
import cc.lixiaoyu.wanandroid.util.ToastUtil
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubKnowledgeNodeViewModel(private val cid: String) : ViewModel() {

    private val apiService = RetrofitManager.wanAndroidService

    private var currentPage = 0

    private val _articleList: MutableStateFlow<List<Article>> = MutableStateFlow(emptyList())
    val articleList: StateFlow<List<Article>> = _articleList

    private val _isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _isLoadingMore: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore


    init {
        refreshArticleListByCid()
    }

    fun refreshArticleListByCid() {
        currentPage = 0
        _isRefreshing.value = true
        viewModelScope.launch {
            try {
                val articlePage = apiService.getArticleListByCid(0, cid).data
                articlePage?.let {
                    currentPage = it.curPage
                    _articleList.value = it.dataList
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                ToastUtil.showToast("加载内容出错了~")
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun loadMoreArticleListByCid() {
        if (_isLoadingMore.value) return
        _isLoadingMore.value = true
        currentPage++
        viewModelScope.launch {
            try {
                val articlePage = apiService.getArticleListByCid(currentPage, cid).data
                articlePage?.let {
                    currentPage = it.curPage
                    val list = _articleList.value.toMutableList()
                    list.addAll(it.dataList)
                    _articleList.value = list
                }
            } catch (e: Throwable) {
                currentPage--
                e.printStackTrace()
                ToastUtil.showToast("加载内容出错了~")
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    fun collectOrUnCollectArticle(context: Context, position: Int, article: Article) {
        if (article.isCollect) {
            CollectAbility.unCollectArticle(context, article.id) { success ->
                if (success) {
                    article.isCollect = false
                    _articleList.value = _articleList.value
                    ToastUtil.showToast(context.getString(R.string.uncollect_success))
                } else {
                    ToastUtil.showToast(context.getString(R.string.uncollect_fail))
                }
            }
        } else {
            CollectAbility.collectArticle(context, article.id) { success ->
                if (success) {
                    article.isCollect = true
                    _articleList.value = _articleList.value
                    ToastUtil.showToast(context.getString(R.string.collect_success))
                } else {
                    ToastUtil.showToast(context.getString(R.string.uncollect_fail))
                }
            }
        }
    }

}

class SubKnowledgeNodeViewModelFactory(private val cid: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return SubKnowledgeNodeViewModel(cid) as T
    }
}