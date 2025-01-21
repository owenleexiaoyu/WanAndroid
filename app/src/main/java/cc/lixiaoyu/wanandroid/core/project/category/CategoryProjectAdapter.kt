package cc.lixiaoyu.wanandroid.core.project.category

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.core.detail.ArticleDetailActivity
import cc.lixiaoyu.wanandroid.databinding.ItemRecyclerviewProjectDataBinding
import cc.lixiaoyu.wanandroid.entity.Article
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseViewHolder

class CategoryProjectAdapter(private val viewModel: CategoryProjectViewModel) : RecyclerView.Adapter<CategoryProjectAdapter.ViewHolder>() {

    private val data: MutableList<Article> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerviewProjectDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    fun setNewData(dataList: List<Article>) {
        data.clear()
        data.addAll(dataList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemRecyclerviewProjectDataBinding) : BaseViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                ArticleDetailActivity.actionStart(it.context, data[adapterPosition].toDetailParam())
            }
            binding.itemProjectCollect.setOnClickListener {
                val article = data[adapterPosition]
                if (article.isCollect) {
                    viewModel.unCollectArticleInArticleList(it.context, adapterPosition, article.id)
                } else {
                    viewModel.collectArticleInArticleList(it.context, adapterPosition, article.id)
                }
            }
        }

        fun bindData(article: Article) {
            binding.itemProjectAuthor.text = article.author
            binding.itemProjectTitle.text = Html.fromHtml(article.title)
            binding.itemProjectDesc.text = article.desc
            binding.itemProjectTime.text = article.niceDate
            if (article.envelopePic.isNullOrEmpty()) {
                binding.itemProjectImg.visibility = View.GONE
            } else {
                binding.itemProjectImg.apply {
                    visibility = View.VISIBLE
                    val options = RequestOptions().centerCrop()
                    Glide.with(context).load(article.envelopePic)
                        .apply(options).into(this)
                }
            }
            //设置文章是否被收藏
            binding.itemProjectCollect.apply {
                setColorFilter(ContextCompat.getColor(context, R.color.ConstBrand))
                setImageResource(if (article.isCollect) R.drawable.ic_favorite_full else R.drawable.ic_favorite_border)
            }
        }
    }
}