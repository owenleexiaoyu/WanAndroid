package cc.lixiaoyu.wanandroid.core.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private val _currentTabIndex: MutableLiveData<Int> = MutableLiveData()
    val currentTabIndex: LiveData<Int>
        get() = _currentTabIndex

    init {
        _currentTabIndex.value = 0
    }

    fun changeCurrentIndex(index: Int) {
        if (_currentTabIndex.value != index) {
            _currentTabIndex.value = index
        }
    }

}