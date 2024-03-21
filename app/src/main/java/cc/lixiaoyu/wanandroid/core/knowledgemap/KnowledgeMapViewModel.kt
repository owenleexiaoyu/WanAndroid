package cc.lixiaoyu.wanandroid.core.knowledgemap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.lixiaoyu.wanandroid.api.WanAndroidService
import cc.lixiaoyu.wanandroid.core.knowledgemap.model.KnowledgeNode
import cc.lixiaoyu.wanandroid.util.ToastUtil
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class KnowledgeMapViewModel: ViewModel() {

    private val apiService: WanAndroidService by lazy {
        RetrofitManager.wanAndroidService
    }

    private val _knowledgeNodeList: MutableStateFlow<List<KnowledgeNode>> = MutableStateFlow(
        emptyList()
    )
    val knowledgeNodeList: StateFlow<List<KnowledgeNode>> = _knowledgeNodeList

    init {
        getKnowledgeMap()
    }

    private fun getKnowledgeMap() {
        viewModelScope.launch {
            try {
                val list = apiService.getKnowledgeMap().data
                _knowledgeNodeList.value = list ?: emptyList()
            } catch (t: Throwable) {
                t.printStackTrace()
                ToastUtil.showToast("出错了，请稍后再试~")
            }
        }
    }
}