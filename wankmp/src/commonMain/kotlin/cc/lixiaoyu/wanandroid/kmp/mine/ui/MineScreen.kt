package cc.lixiaoyu.wanandroid.kmp.mine.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Verified
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

data class MineUser(
    val name: String,
    val id: String,
)

sealed interface MineAction {
    data object ClickProfile : MineAction
    data object ClickMyPoints : MineAction
    data object ClickPointsRank : MineAction
    data object ClickMyShare : MineAction
    data object ClickMyCollect : MineAction
    data object ClickBrowseHistory : MineAction
    data object ClickOpenSourceLicense : MineAction
    data object ClickAboutAuthor : MineAction
    data object ClickSystemSettings : MineAction
}

/**
 * 个人页（我的）：标题 + 用户卡片 + 菜单列表（与截图结构一致）。
 *
 * @param onAction 点击事件统一向外抛，宿主决定跳转/登录等行为。
 */
@Composable
fun MineScreen(
    user: MineUser?,
    modifier: Modifier = Modifier,
    showTopBar: Boolean = true,
    title: String = "我的",
    onAction: (MineAction) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Medium,
                        )
                    },
                    backgroundColor = Color.White,
                    elevation = 1.dp,
                )
            }
        },
        backgroundColor = Color(0xFFF5F5F5),
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(vertical = 10.dp),
        ) {
            item {
                MineProfileCard(
                    user = user,
                    onClick = { onAction(MineAction.ClickProfile) },
                )
            }

            item { Spacer(Modifier.height(10.dp)) }

            val items = mineMenuItems()
            items(items, key = { it.key }) { it ->
                MineMenuRow(
                    icon = it.icon,
                    title = it.title,
                    showDivider = it.showDivider,
                    onClick = { onAction(it.action) },
                )
            }
        }
    }
}

@Composable
private fun MineProfileCard(
    user: MineUser?,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(color = Color(0xFFE9E9E9), shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(44.dp),
                    tint = Color(0xFF9E9E9E),
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = user?.name ?: "点击登录",
                    color = Color(0xFF222222),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (user != null) "ID: ${user.id}" else "登录后查看 ID",
                    color = Color(0xFF888888),
                    fontSize = 13.sp,
                )
            }

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFB0B0B0),
            )
        }

        Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
    }
}

private data class MineMenuItem(
    val key: String,
    val icon: ImageVector,
    val title: String,
    val action: MineAction,
    val showDivider: Boolean,
)

private fun mineMenuItems(): List<MineMenuItem> = listOf(
    MineMenuItem(
        key = "my_points",
        icon = Icons.Filled.Verified,
        title = "我的积分",
        action = MineAction.ClickMyPoints,
        showDivider = true,
    ),
    MineMenuItem(
        key = "points_rank",
        icon = Icons.Filled.Leaderboard,
        title = "积分排行",
        action = MineAction.ClickPointsRank,
        showDivider = true,
    ),
    MineMenuItem(
        key = "my_share",
        icon = Icons.Filled.Share,
        title = "我的分享",
        action = MineAction.ClickMyShare,
        showDivider = true,
    ),
    MineMenuItem(
        key = "my_collect",
        icon = Icons.Filled.StarBorder,
        title = "我的收藏",
        action = MineAction.ClickMyCollect,
        showDivider = true,
    ),
    MineMenuItem(
        key = "browse_history",
        icon = Icons.Filled.History,
        title = "浏览历史",
        action = MineAction.ClickBrowseHistory,
        showDivider = true,
    ),
    MineMenuItem(
        key = "open_source_license",
        icon = Icons.Filled.HelpOutline,
        title = "开源许可",
        action = MineAction.ClickOpenSourceLicense,
        showDivider = true,
    ),
    MineMenuItem(
        key = "about_author",
        icon = Icons.Filled.Info,
        title = "关于作者",
        action = MineAction.ClickAboutAuthor,
        showDivider = true,
    ),
    MineMenuItem(
        key = "system_settings",
        icon = Icons.Filled.Settings,
        title = "系统设置",
        action = MineAction.ClickSystemSettings,
        showDivider = false,
    ),
)

@Composable
private fun MineMenuRow(
    icon: ImageVector,
    title: String,
    showDivider: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF333333),
            )
            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp),
                color = Color(0xFF222222),
                fontSize = 15.sp,
            )
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFB0B0B0),
            )
        }

        if (showDivider) {
            Divider(
                modifier = Modifier.padding(start = 16.dp),
                color = Color(0xFFEEEEEE),
                thickness = 1.dp,
            )
        }
    }
}

@Preview
@Composable
private fun MineScreenPreview() {
    MaterialTheme {
        MineScreen(
            user = MineUser(name = "小小的太太阳", id = "27165"),
            onAction = {},
        )
    }
}

