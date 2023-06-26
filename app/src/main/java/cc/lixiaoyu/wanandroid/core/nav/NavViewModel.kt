package cc.lixiaoyu.wanandroid.core.nav

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cc.lixiaoyu.wanandroid.BuildConfig
import cc.lixiaoyu.wanandroid.api.WanAndroidService
import cc.lixiaoyu.wanandroid.entity.Nav
import cc.lixiaoyu.wanandroid.util.ToastUtil
import cc.lixiaoyu.wanandroid.util.network.BaseModelFactory
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager

class NavViewModel : ViewModel() {

    private val _navList: MutableLiveData<List<Nav>> = MutableLiveData()
    val navList: LiveData<List<Nav>> = _navList

    private val _currentPosition: MutableLiveData<Int> = MutableLiveData()
    val currentPosition: LiveData<Int> = _currentPosition

    private val wanAndroidService: WanAndroidService by lazy {
        RetrofitManager.getInstance().wanAndroidService
    }

    init {
        _currentPosition.value = 0
        fetchNavData()
    }

    @SuppressLint("CheckResult")
    private fun fetchNavData() {
        BaseModelFactory.compose(wanAndroidService.getNavData())
            .subscribe(
                { result ->
                    _navList.value = result.get()
                },
                { t ->
                    ToastUtil.showToast("请求出错")
                    if (BuildConfig.DEBUG) {
                        t.printStackTrace()
                    }
                })
    }

    fun changeCurrentPosition(position: Int) {
        if (position < 0 || position >= (_navList.value?.size ?: 0)) {
            return
        }
        _currentPosition.value = position
    }
}