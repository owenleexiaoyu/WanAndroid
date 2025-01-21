package cc.lixiaoyu.wanandroid.core.collection

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.core.detail.ArticleDetailActivity.Companion.actionStart
import cc.lixiaoyu.wanandroid.databinding.ItemRecyclerviewCollectionBinding
import cc.lixiaoyu.wanandroid.entity.Article

class CollectionAdapter(private val viewModel: CollectionViewModel) :
    RecyclerView.Adapter<CollectionAdapter.ViewHolder>() {

    private val data: MutableList<Article> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerviewCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = data[position]
        holder.bindData(article)
    }

    fun setNewData(dataList: List<Article>) {
        data.clear()
        data.addAll(dataList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemRecyclerviewCollectionBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val article = data[adapterPosition]
                actionStart(it.context, article.toDetailParam())
            }
            binding.itemCollectionCollect.setOnClickListener {
                val article = data[adapterPosition]
                viewModel.unCollectArticle(it.context, adapterPosition, article.id)
            }
        }

        fun bindData(article: Article) {
            binding.itemCollectionAuthor.text = article.author
            binding.itemCollectionTime.text = article.niceDate
            binding.itemCollectionTitle.text = Html.fromHtml(article.title)
            binding.itemCollectionChapter.text = article.chapterName
            binding.itemCollectionCollect.setImageResource(R.drawable.ic_favorite_full)
        }
    }
}