package cc.lixiaoyu.wanandroid.core.settings

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cc.lixiaoyu.wanandroid.core.theme.AppThemeController
import cc.lixiaoyu.wanandroid.kmp.AndroidToastContext
import cc.lixiaoyu.wanandroid.kmp.settings.ui.SettingsAction
import cc.lixiaoyu.wanandroid.kmp.settings.ui.SettingsScreen
import cc.lixiaoyu.wanandroid.kmp.theme.WanTheme
import cc.lixiaoyu.wanandroid.util.ToastUtil

class KmpSettingsActivity : AppCompatActivity() {

    private val themeController = AppThemeController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidToastContext.init(applicationContext)
        setContent {
            val isDarkMode by themeController.isDarkMode.collectAsState()
            WanTheme(darkTheme = isDarkMode) {
                SettingsScreen(
                    themeController = themeController,
                    onBack = { finish() },
                    onItemClick = { action ->
                        ToastUtil.showToast(action.toastMessage())
                    },
                )
            }
        }
    }
}

private fun SettingsAction.toastMessage(): String = when (this) {
    SettingsAction.FontSize -> "点击：字体大小"
    SettingsAction.ClearCache -> "点击：清除缓存"
    SettingsAction.CheckVersion -> "点击：检查版本"
    SettingsAction.AboutUs -> "点击：关于我们"
    SettingsAction.Logout -> "点击：退出登录"
}
