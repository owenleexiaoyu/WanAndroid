package cc.lixiaoyu.wanandroid.kmp.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.lixiaoyu.wanandroid.kmp.theme.wanColors

/** Content-sized tabs: no Material Tab/ScrollableTabRow minimum width. */
@Composable
fun ChapterCapsuleTabs(chapters: List<Chapter>, selectedId: Int, onSelect: (Chapter) -> Unit) {
    val list = rememberLazyListState()
    LaunchedEffect(selectedId, chapters) {
        val index = chapters.indexOfFirst { it.id == selectedId }
        if (index >= 0) list.animateScrollToItem(index)
    }
    LazyRow(state = list, modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(chapters, key = { it.id }) { chapter ->
            ChapterCapsule(chapter, chapter.id == selectedId) { onSelect(chapter) }
        }
    }
}

@Composable
fun ChapterCapsule(chapter: Chapter, selected: Boolean, onClick: () -> Unit) {
    val colors = MaterialTheme.wanColors
    Text(chapter.name.plainText(), fontSize = 13.sp,
        color = if (selected) colors.textPrimary else colors.textSecondary,
        modifier = Modifier
            .background(if (selected) colors.divider else colors.pageBackground, RoundedCornerShape(20.dp))
            .selectable(selected, role = Role.Tab, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 6.dp))
}
