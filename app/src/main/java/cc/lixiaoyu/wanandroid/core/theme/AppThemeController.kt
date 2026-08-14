package cc.lixiaoyu.wanandroid.core.theme

import androidx.appcompat.app.AppCompatDelegate
import cc.lixiaoyu.wanandroid.kmp.theme.ThemeController
import kotlinx.coroutines.flow.StateFlow

object AppThemeController : ThemeController {
    override val isDarkMode: StateFlow<Boolean> = ThemeManager.isDarkMode

    override fun setDarkMode(isDarkMode: Boolean) {
        ThemeManager.setDarkMode(isDarkMode)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            },
        )
    }
}
