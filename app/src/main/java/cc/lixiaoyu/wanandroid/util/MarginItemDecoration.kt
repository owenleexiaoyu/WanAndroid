package cc.lixiaoyu.wanandroid.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(
    private val spaceInPx: Int,
    private val spanCount: Int = 1,
    @RecyclerView.Orientation private val orientation: Int = RecyclerView.VERTICAL
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        with(outRect) {
            if (orientation == RecyclerView.VERTICAL) {
                if (parent.getChildAdapterPosition(view) < spanCount) {
                    top = spaceInPx
                }
                if (parent.getChildAdapterPosition(view) % spanCount == 0) {
                    left = spaceInPx
                }
            } else {
                if (parent.getChildAdapterPosition(view) < spanCount) {
                    left = spaceInPx
                }
                if (parent.getChildAdapterPosition(view) % spanCount == 0) {
                    top = spaceInPx
                }
            }

            right = spaceInPx
            bottom = spaceInPx
        }
    }
}