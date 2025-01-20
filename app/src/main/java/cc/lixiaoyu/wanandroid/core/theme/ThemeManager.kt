package cc.lixiaoyu.wanandroid.core.theme

import cc.lixiaoyu.wanandroid.util.AppConst
import cc.lixiaoyu.wanandroid.util.storage.SPUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ThemeManager {

    private val _isDarkMode: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        _isDarkMode.value = loadValueFromStorage()
    }

    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    fun setDarkMode(isDarkMode: Boolean) {
        if (isDarkMode == _isDarkMode.value) {
            return
        }
        SPUtil.saveData(AppConst.SP_KEY_DARK_MODE, isDarkMode)
        _isDarkMode.value = isDarkMode
    }

    private fun loadValueFromStorage(): Boolean {
        return try {
            SPUtil.getData(AppConst.SP_KEY_DARK_MODE, false) as Boolean
        } catch (e: Exception) {
            false
        }
    }
}