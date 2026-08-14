package cc.lixiaoyu.wanandroid.kmp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.lixiaoyu.wanandroid.kmp.theme.wanColors

@Composable
fun WanListSection(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.wanColors.surface),
        content = content,
    )
}

@Composable
fun WanListRow(
    title: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    value: String? = null,
    showChevron: Boolean = true,
    showDivider: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val clickableModifier = if (onClick != null) {
        modifier.clickable(onClick = onClick)
    } else {
        modifier
    }

    Column(
        modifier = clickableModifier
            .fillMaxWidth()
            .background(MaterialTheme.wanColors.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 54.dp)
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.wanColors.icon,
                )
            }

            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = if (icon != null) 14.dp else 0.dp),
                color = MaterialTheme.wanColors.textPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
            )

            if (value != null) {
                Text(
                    text = value,
                    color = MaterialTheme.wanColors.textPrimary,
                    fontSize = 16.sp,
                )
            }

            trailing?.invoke()

            if (showChevron) {
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.wanColors.textSecondary,
                    modifier = Modifier.padding(start = 6.dp),
                )
            }
        }

        if (showDivider) {
            Divider(
                modifier = Modifier.padding(start = if (icon != null) 16.dp else 0.dp),
                color = MaterialTheme.wanColors.divider,
                thickness = 1.dp,
            )
        }
    }
}

@Composable
fun WanCenterListRow(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.wanColors.surface)
            .clickable(onClick = onClick)
            .heightIn(min = 60.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            color = MaterialTheme.wanColors.textPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}
