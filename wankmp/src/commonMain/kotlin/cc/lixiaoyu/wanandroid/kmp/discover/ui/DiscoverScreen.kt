package cc.lixiaoyu.wanandroid.kmp.discover.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.lixiaoyu.wanandroid.kmp.core.ui.WanAsyncImage
import cc.lixiaoyu.wanandroid.kmp.discover.domain.DiscoverBanner
import cc.lixiaoyu.wanandroid.kmp.discover.domain.PopularItem
import cc.lixiaoyu.wanandroid.kmp.discover.domain.PopularSection
import cc.lixiaoyu.wanandroid.kmp.discover.domain.PopularSectionType
import cc.lixiaoyu.wanandroid.kmp.discover.mvi.DiscoverEffect
import cc.lixiaoyu.wanandroid.kmp.discover.mvi.DiscoverIntent
import cc.lixiaoyu.wanandroid.kmp.discover.mvi.DiscoverStore
import cc.lixiaoyu.wanandroid.kmp.theme.wanColors
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun DiscoverScreen(
    store: DiscoverStore,
    modifier: Modifier = Modifier,
    onOpenUrl: (DiscoverEffect.OpenUrl) -> Unit,
    onShowToast: (String) -> Unit,
) {
    val uiState by store.state.collectAsState()

    LaunchedEffect(store) {
        coroutineScope {
            launch {
                store.effect.collect { effect ->
                    when (effect) {
                        is DiscoverEffect.OpenUrl -> onOpenUrl(effect)
                        is DiscoverEffect.ShowToast -> onShowToast(effect.message)
                    }
                }
            }
            store.dispatch(DiscoverIntent.Enter)
        }
    }

    Scaffold(
        modifier = modifier,
        backgroundColor = MaterialTheme.wanColors.pageBackground,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.errorMessage != null -> {
                    DiscoverError(
                        message = uiState.errorMessage.orEmpty(),
                        onRetry = { store.dispatch(DiscoverIntent.Retry) },
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        item {
                            DiscoverBannerCarousel(
                                banners = uiState.banners,
                                currentIndex = uiState.currentBannerIndex,
                                onPageChanged = {
                                    store.dispatch(DiscoverIntent.BannerPageChanged(it))
                                },
                                onBannerClick = {
                                    store.dispatch(DiscoverIntent.ClickBanner(it))
                                },
                            )
                        }
                        items(uiState.sections, key = { it.type.name }) { section ->
                            PopularSectionCard(
                                section = section,
                                onItemClick = {
                                    store.dispatch(DiscoverIntent.ClickPopularItem(it))
                                },
                                onMoreClick = {
                                    store.dispatch(DiscoverIntent.ClickMore(section.type))
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DiscoverError(
    message: String,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.body1,
        )
        TextButton(
            onClick = onRetry,
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text("重试")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DiscoverBannerCarousel(
    banners: List<DiscoverBanner>,
    currentIndex: Int,
    onPageChanged: (Int) -> Unit,
    onBannerClick: (DiscoverBanner) -> Unit,
) {
    if (banners.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2.1f)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.wanColors.surface),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "暂无 Banner",
                color = MaterialTheme.wanColors.textSecondary,
            )
        }
        return
    }

    val pagerState = rememberPagerState(pageCount = { banners.size })

    LaunchedEffect(pagerState.currentPage) {
        if (currentIndex != pagerState.currentPage) {
            onPageChanged(pagerState.currentPage)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2.1f)
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.wanColors.surface),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            val banner = banners[page]
            WanAsyncImage(
                url = banner.imageUrl,
                contentDescription = banner.title,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onBannerClick(banner) },
                contentScale = ContentScale.Crop,
            )
        }

        val title = banners.getOrNull(pagerState.currentPage)?.title.orEmpty()
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.32f))
                .padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
            )
            Text(
                text = "${pagerState.currentPage + 1}/${banners.size}",
                color = Color.White,
                fontSize = 11.sp,
            )
        }
    }
}

@Composable
private fun PopularSectionCard(
    section: PopularSection,
    onItemClick: (PopularItem) -> Unit,
    onMoreClick: () -> Unit,
) {
    val spec = section.uiSpec()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.wanColors.surface)
            .border(
                width = 0.5.dp,
                color = MaterialTheme.wanColors.divider.copy(alpha = 0.45f),
                shape = RoundedCornerShape(6.dp),
            )
            .padding(horizontal = 16.dp, vertical = 15.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color = spec.accentColor, shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = spec.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp),
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp),
            ) {
                Text(
                    text = section.title,
                    color = MaterialTheme.wanColors.textPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .size(width = 34.dp, height = 2.dp)
                        .background(spec.accentColor),
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .clickable(onClick = onMoreClick),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = section.moreLabel,
                    color = MaterialTheme.wanColors.textSecondary,
                    fontSize = 14.sp,
                )
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.wanColors.textSecondary,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        Spacer(Modifier.height(13.dp))
        section.items.take(3).forEach { item ->
            PopularItemRow(
                item = item,
                accentColor = spec.accentColor,
                onClick = { onItemClick(item) },
            )
        }
    }
}

@Composable
private fun PopularItemRow(
    item: PopularItem,
    accentColor: Color,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(color = accentColor, shape = CircleShape),
        )
        Text(
            text = item.title,
            modifier = Modifier.padding(start = 14.dp),
            color = MaterialTheme.wanColors.textPrimary,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private data class PopularSectionUiSpec(
    val accentColor: Color,
    val icon: ImageVector,
)

private fun PopularSection.uiSpec(): PopularSectionUiSpec = when (type) {
    PopularSectionType.Route -> PopularSectionUiSpec(
        accentColor = Color(0xFF2D91B8),
        icon = Icons.Filled.Book,
    )
    PopularSectionType.Wenda -> PopularSectionUiSpec(
        accentColor = Color(0xFF41C936),
        icon = Icons.Filled.HelpOutline,
    )
    PopularSectionType.Column -> PopularSectionUiSpec(
        accentColor = Color(0xFFFFA000),
        icon = Icons.Filled.Edit,
    )
}
