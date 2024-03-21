package cc.lixiaoyu.wanandroid.core.nav

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import cc.lixiaoyu.wanandroid.BuildConfig
import cc.lixiaoyu.wanandroid.api.WanAndroidService
import cc.lixiaoyu.wanandroid.util.ToastUtil
import cc.lixiaoyu.wanandroid.util.network.BaseModelFactory
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager

class NavViewModel : ViewModel() {

    private val wanAndroidService: WanAndroidService by lazy {
        RetrofitManager.wanAndroidService
    }

    /**
     * 从服务端获取的 NavList 数据，包装成 LiveData，当 [_navList] 中的数据更新时，会触发
     * [navTitleList] 和 [currentNavItemList] 两个 LiveData 的更新
      */
    private val _navList: MutableLiveData<List<Nav>> = MutableLiveData(listOf())

    /**
     * 表示当前选中的 Nav 的 position，当用户在 RecycleView 中选中不同的 Nav，触发 [_currentPosition] 的数据更新，
     * 会触发 [currentNavItemList] 这个 Livedata 的更新
     */
    private val _currentPosition: MutableLiveData<Int> = MutableLiveData(0)

    fun getCurrentPosition(): LiveData<Int> = _currentPosition

    fun changeCurrentPosition(position: Int) {
        _currentPosition.value = position
    }

    /**
     * 将 [_navList] 中每个 Nav 的 name 取出组成一个 List 并包装成 LiveData 供界面订阅，去除冗余信息
     */
    val navTitleList: LiveData<List<String>> = _navList.map { navList ->
        navList.map { it.name }.toList()
    }

    /**
     * 使用 [MediatorLiveData] 监听 [_navList] 和 [_currentPosition] 这两个上游 LiveData 的变化，
     * 当它们其中一个发生变化，就会触发这个 LiveData 的数据更新
     *
     * Notes：本来是使用 Transformations.switchMap(_currentPosition) 来监听 _currentPosition 的变化
     * 但这样实现在首次进入页面，拉取网络请求更新 _navList 后，这个数据不会更新，因此需要监听两个上游
     */
    val currentNavItemList: MediatorLiveData<List<NavItem>> = MediatorLiveData()

    private fun addSourcesForCurrentNavItemList() {
        currentNavItemList.addSource(_currentPosition) {
            val navList = _navList.value
            if (navList.isNullOrEmpty()) {
                return@addSource
            }
            currentNavItemList.value = navList[it].items
        }
        currentNavItemList.addSource(_navList) {
            if (it.isEmpty()) {
                return@addSource
            }
            currentNavItemList.value = it[_currentPosition.value ?: 0].items
        }
    }

    init {
        fetchNavData()
        addSourcesForCurrentNavItemList()
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
}