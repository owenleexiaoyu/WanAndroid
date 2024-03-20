package cc.lixiaoyu.wanandroid.core.todo.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.lixiaoyu.wanandroid.core.todo.model.TodoEntity.TodoItem
import cc.lixiaoyu.wanandroid.databinding.ItemRecyclerviewTodoDoneBinding

class TodoFinishedAdapter :
    RecyclerView.Adapter<TodoFinishedAdapter.ViewHolder>() {

    private val data: MutableList<TodoItem> = mutableListOf()

    var itemClickListener: TodoFinishedItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerviewTodoDoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = data[position]
        if (position > 0 && data[position - 1].date == item.date) {
            holder.binding.itemDoneTvTime.visibility = View.GONE
            holder.binding.itemDoneLine.visibility = View.GONE
        } else {
            holder.binding.itemDoneTvTime.text = item.dateStr
            holder.binding.itemDoneTvTime.visibility = View.VISIBLE
            holder.binding.itemDoneLine.visibility = View.VISIBLE
        }
        holder.binding.itemDoneTvTitle.text = item.title
        if (!TextUtils.isEmpty(item.content)) {
            holder.binding.itemDoneTvDesc.text = item.content
            holder.binding.itemDoneTvDesc.visibility = View.VISIBLE
        } else {
            holder.binding.itemDoneTvDesc.visibility = View.GONE
        }
    }

    fun setNewData(data: List<TodoItem>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemRecyclerviewTodoDoneBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemDoneBtnUnfinish.setOnClickListener {
                this@TodoFinishedAdapter.itemClickListener?.onUnFinish(data[adapterPosition], adapterPosition)
            }
            binding.itemDoneBtnDelete.setOnClickListener {
                this@TodoFinishedAdapter.itemClickListener?.onDelete(data[adapterPosition], adapterPosition)
            }
            binding.root.setOnClickListener {
                this@TodoFinishedAdapter.itemClickListener?.onUpdate(data[adapterPosition], adapterPosition)
            }
        }
    }

}

interface TodoFinishedItemClickListener {

    fun onDelete(item: TodoItem, position: Int)

    fun onUnFinish(item: TodoItem, position: Int)

    fun onUpdate(item: TodoItem, position: Int)
}