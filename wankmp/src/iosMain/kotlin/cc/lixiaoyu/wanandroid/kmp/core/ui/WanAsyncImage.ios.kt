package cc.lixiaoyu.wanandroid.kmp.core.ui

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import cc.lixiaoyu.wanandroid.kmp.theme.wanColors
import androidx.compose.material.MaterialTheme

@Composable
actual fun WanAsyncImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale,
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier.background(MaterialTheme.wanColors.surface),
    )
}
