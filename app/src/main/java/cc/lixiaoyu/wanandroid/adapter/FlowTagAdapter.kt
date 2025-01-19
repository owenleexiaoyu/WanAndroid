package cc.lixiaoyu.wanandroid.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cc.lixiaoyu.wanandroid.R

class FlowTagAdapter<T>: RecyclerView.Adapter<FlowTagAdapter<T>.NavItemViewHolder>() {

    private val dataList: MutableList<T> = mutableListOf()
    private var tagClickLister: ((view: View, position: Int, data: T) -> Unit)? = null
    private var titleConverter: ((data: T) -> String) = { "" }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_nav_item, parent, false)
        return NavItemViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: NavItemViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    fun setTitleConverter(converter: (data: T) -> String) {
        titleConverter = converter
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<T>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    fun setOnTagClickListener(listener: (view: View, position: Int, data: T) -> Unit) {
        // Handle click event
        tagClickLister = listener
    }

    inner class NavItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val navItemTitle: TextView? = itemView as? TextView
        private var currentItem: T? = null

        init {
            itemView.setOnClickListener {
                // Handle click event
                currentItem?.let { item ->
                    // Handle click event
                    tagClickLister?.invoke(itemView, adapterPosition, item)
                }

            }
        }

        fun bind(data: T) {
            currentItem = data
            navItemTitle?.text = titleConverter.invoke(data)
        }
    }
}