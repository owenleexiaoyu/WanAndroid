package cc.lixiaoyu.wanandroid.core.detail.more

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import cc.lixiaoyu.wanandroid.R

class DetailMoreAdapter(dataList: List<DetailMoreItem>): Adapter<DetailMoreAdapter.DetailMoreItemViewHolder>() {

    private val dataList: MutableList<DetailMoreItem> = mutableListOf()

    init {
        this.dataList.addAll(dataList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailMoreItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_more_item, parent, false)
        return DetailMoreItemViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: DetailMoreItemViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    inner class DetailMoreItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView
        private val imgIcon: ImageView

        init {
            tvTitle = itemView.findViewById(R.id.item_detail_more_title)
            imgIcon = itemView.findViewById(R.id.item_detail_more_icon)
            itemView.setOnClickListener {
                dataList[adapterPosition].onClick?.invoke(it)
            }
        }

        fun bind(data: DetailMoreItem) {
            tvTitle.text = data.title
            imgIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context, data.icon))
        }
    }
}

data class DetailMoreItem(
    val title: String,
    @DrawableRes val icon: Int,
    val onClick: ((View) -> Unit)?
)