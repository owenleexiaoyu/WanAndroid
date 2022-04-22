package cc.lixiaoyu.wanandroid.core.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cc.lixiaoyu.wanandroid.entity.Integral
import cc.lixiaoyu.wanandroid.entity.IntegralRecord
import cc.lixiaoyu.wanandroid.entity.PageData
import cc.lixiaoyu.wanandroid.base.BaseViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception

class MyIntegralViewModel(val repository: MyIntegralRepository) : BaseViewModel() {

    // 积分信息
    val integralInfo: MutableLiveData<Integral> = MutableLiveData()

    // 积分获取记录
    val integralRecordList: MutableLiveData<MutableList<IntegralRecord>> = MutableLiveData()

    val refreshStatus: MutableLiveData<Boolean> = MutableLiveData()
    val reloadStatus: MutableLiveData<Boolean> = MutableLiveData()
    val loadMoreStatus: MutableLiveData<Int> = MutableLiveData()

    // 积分获取记录的当前页
    var _currentRecordPage: Int = 0

    fun getIntegralInfoDeferred(): Deferred<Integral> {
        return viewModelScope.async {
            repository.getMyIntegralInfo()
        }
    }

    fun getIntegralRecordListDeferred(page: Int): Deferred<PageData<IntegralRecord>> {
        return viewModelScope.async {
            repository.getMyIntegralRecord(page)
        }
    }

    /**
     * 进入【我的积分】页面、手动刷新时调用
     *
     * 刷新数据，同时获取积分信息和第一页的积分记录
     */
    fun refresh() {
        viewModelScope.launch {
            try {
                val info = getIntegralInfoDeferred().await()
                val firstPageRecord = getIntegralRecordListDeferred(0).await()
                integralInfo.value = info
                updateIntegralRecord(firstPageRecord, false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 上拉加载时调用
     *
     * 加载更多积分记录
     */
    fun loadMoreRecord() {
        launch(
                block = {
                    val pageRecord = getIntegralRecordListDeferred(_currentRecordPage++).await()
                    updateIntegralRecord(pageRecord, true)
                },
                error = {
                    it.printStackTrace()
                }
        )
    }

    /**
     * 更新积分记录
     *
     * @param pageRecord 分页的积分记录
     * @param append 是否追加到 integralRecordList.value 的后面
     */
    private fun updateIntegralRecord(pageRecord: PageData<IntegralRecord>, append: Boolean) {
        _currentRecordPage = pageRecord.curPage
        val recordList = pageRecord.datas
        if (append) {
            val oldList = integralRecordList.value
            oldList?.addAll(recordList)
            integralRecordList.value = oldList
        } else {
            integralRecordList.value = recordList.toMutableList()
        }
    }
}