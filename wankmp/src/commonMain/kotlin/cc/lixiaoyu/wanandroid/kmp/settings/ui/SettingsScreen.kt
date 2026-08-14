package cc.lixiaoyu.wanandroid.kmp.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cc.lixiaoyu.wanandroid.kmp.core.ui.WanCenterListRow
import cc.lixiaoyu.wanandroid.kmp.core.ui.WanListRow
import cc.lixiaoyu.wanandroid.kmp.core.ui.WanListSection
import cc.lixiaoyu.wanandroid.kmp.theme.ThemeController
import cc.lixiaoyu.wanandroid.kmp.theme.wanColors

@Composable
fun SettingsScreen(
    themeController: ThemeController,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onItemClick: (SettingsAction) -> Unit,
) {
    val isDarkMode by themeController.isDarkMode.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.wanColors.surface),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                ) {
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterStart),
                        onClick = onBack,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "返回",
                            tint = MaterialTheme.wanColors.textPrimary,
                        )
                    }
                    Text(
                        text = "系统设置",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.wanColors.textPrimary,
                    )
                }
                Divider(color = MaterialTheme.wanColors.divider, thickness = 1.dp)
            }
        },
        backgroundColor = MaterialTheme.wanColors.pageBackground,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.wanColors.pageBackground)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(12.dp))
            WanListSection {
                WanListRow(
                    title = "夜间模式",
                    showChevron = false,
                    showDivider = true,
                    trailing = {
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { checked: Boolean ->
                                themeController.setDarkMode(checked)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.wanColors.brand,
                                checkedTrackColor = MaterialTheme.wanColors.brand.copy(alpha = 0.45f),
                            ),
                        )
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    onClick = {
                        themeController.setDarkMode(!isDarkMode)
                    },
                )
                WanListRow(
                    title = "字体大小",
                    value = "100%",
                    showChevron = false,
                    showDivider = true,
                    onClick = { onItemClick(SettingsAction.FontSize) },
                )
                WanListRow(
                    title = "清除缓存",
                    value = "3.33MB",
                    showChevron = false,
                    showDivider = false,
                    onClick = { onItemClick(SettingsAction.ClearCache) },
                )
            }

            Spacer(Modifier.height(10.dp))
            WanListSection {
                WanListRow(
                    title = "检查版本",
                    value = "已是最新版",
                    showChevron = false,
                    showDivider = true,
                    onClick = { onItemClick(SettingsAction.CheckVersion) },
                )
                WanListRow(
                    title = "关于我们",
                    value = "当前版本1.0",
                    showChevron = false,
                    showDivider = false,
                    onClick = { onItemClick(SettingsAction.AboutUs) },
                )
            }

            Spacer(Modifier.height(10.dp))
            WanCenterListRow(
                title = "退出登录",
                onClick = { onItemClick(SettingsAction.Logout) },
            )
        }
    }
}

sealed interface SettingsAction {
    data object FontSize : SettingsAction
    data object ClearCache : SettingsAction
    data object CheckVersion : SettingsAction
    data object AboutUs : SettingsAction
    data object Logout : SettingsAction
}
