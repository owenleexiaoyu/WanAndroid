package cc.lixiaoyu.wanandroid.core.todo.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.lixiaoyu.wanandroid.core.todo.model.TodoEntity.TodoItem
import cc.lixiaoyu.wanandroid.databinding.ItemRecyclerviewTodoUndoneBinding
import com.chad.library.adapter.base.BaseViewHolder

class TodoUnFinishedAdapter :
    RecyclerView.Adapter<TodoUnFinishedAdapter.ViewHolder>() {

    private val data: MutableList<TodoItem> = mutableListOf()

    var itemClickListener: TodoUnfinishedItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerviewTodoUndoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = data[position]
        if (position > 0 && data[position - 1].date == item.date) {
            holder.binding.itemUndoneTvTime.visibility = View.GONE
            holder.binding.itemUndoneLine.visibility = View.GONE
        } else {
            holder.binding.itemUndoneTvTime.text = item.dateStr
            holder.binding.itemUndoneTvTime.visibility = View.VISIBLE
            holder.binding.itemUndoneLine.visibility = View.VISIBLE
        }
        holder.binding.itemUndoneTvTitle.text = item.title
        if (!TextUtils.isEmpty(item.content)) {
            holder.binding.itemUndoneTvDesc.text = item.content
            holder.binding.itemUndoneTvDesc.visibility = View.VISIBLE
        } else {
            holder.binding.itemUndoneTvDesc.visibility = View.GONE
        }
    }

    fun setNewData(data: List<TodoItem>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemRecyclerviewTodoUndoneBinding) : BaseViewHolder(binding.root) {
        init {
            binding.itemUndoneBtnFinish.setOnClickListener {
                this@TodoUnFinishedAdapter.itemClickListener?.onFinish(data[adapterPosition], adapterPosition)
            }
            binding.itemUndoneBtnDelete.setOnClickListener {
                this@TodoUnFinishedAdapter.itemClickListener?.onDelete(data[adapterPosition], adapterPosition)
            }
            binding.root.setOnClickListener {
                this@TodoUnFinishedAdapter.itemClickListener?.onUpdate(data[adapterPosition], adapterPosition)
            }
        }
    }

}

interface TodoUnfinishedItemClickListener {

    fun onDelete(item: TodoItem, position: Int)

    fun onFinish(item: TodoItem, position: Int)

    fun onUpdate(item: TodoItem, position: Int)
}