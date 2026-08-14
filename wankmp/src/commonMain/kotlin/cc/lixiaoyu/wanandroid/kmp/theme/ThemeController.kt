package cc.lixiaoyu.wanandroid.kmp.theme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ThemeController {
    val isDarkMode: StateFlow<Boolean>

    fun setDarkMode(isDarkMode: Boolean)
}

class MemoryThemeController(
    initialDarkMode: Boolean = false,
) : ThemeController {
    private val _isDarkMode = MutableStateFlow(initialDarkMode)

    override val isDarkMode: StateFlow<Boolean> = _isDarkMode

    override fun setDarkMode(isDarkMode: Boolean) {
        _isDarkMode.value = isDarkMode
    }
}

object ThemeControllerRegistry {
    private val fallbackController = MemoryThemeController()

    var controller: ThemeController = fallbackController
        private set

    fun install(controller: ThemeController) {
        this.controller = controller
    }
}
