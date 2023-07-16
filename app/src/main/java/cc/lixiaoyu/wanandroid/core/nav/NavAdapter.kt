package cc.lixiaoyu.wanandroid.core.nav

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import butterknife.BindView
import butterknife.ButterKnife
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.entity.Nav
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class NavAdapter(layoutResId: Int, data: List<String>) :
    BaseQuickAdapter<String, NavAdapter.ViewHolder>(layoutResId, data) {

    var currentItem = 0
        set(value) {
            val oldPosition = field
            field = value
            notifyItemChanged(oldPosition)
            notifyItemChanged(value)
        }

    override fun convert(holder: ViewHolder, item: String) {
        val ctx = holder.itemView.context
        holder.tvTitle.text = item
        if (item === data[currentItem]) {
            holder.tvTitle.setTextColor(ContextCompat.getColor(ctx, R.color.Accent))
        } else {
            holder.tvTitle.setTextColor(ContextCompat.getColor(ctx, R.color.TextPrimary))
        }
    }

    class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.item_nav_title)
    }
}