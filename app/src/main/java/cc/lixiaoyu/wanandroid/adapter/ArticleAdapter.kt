package cc.lixiaoyu.wanandroid.adapter

import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.entity.Article
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ArticleAdapter(layoutResId: Int, data: List<Article?>?) :
    BaseQuickAdapter<Article, ArticleAdapter.ViewHolder>(layoutResId, data) {
    override fun convert(holder: ViewHolder, article: Article) {
        val author = article.author.ifEmpty { article.shareUser.ifEmpty { "Unknown" } }
        holder.tvAuthor.text = author
        holder.tvTime.text = article.niceDate
        holder.tvTitle.text = Html.fromHtml(article.title)
        holder.tvChapter.text = article.chapterName
        if (article.envelopePic.isEmpty()) {
            holder.imgPhoto.visibility = View.GONE
        } else {
            holder.imgPhoto.visibility = View.VISIBLE
            val options = RequestOptions()
                .centerCrop()
            Glide.with(mContext).load(article.envelopePic)
                .apply(options).into(holder.imgPhoto)
        }
        // 设置置顶标签的显示和隐藏
        if (article.type == 0) {
            holder.tvTop.visibility = View.GONE
        } else if (article.type == 1) {
            holder.tvTop.visibility = View.VISIBLE
        }
        // 设置文章是否被收藏
        val ctx = holder.imgCollect.context
        holder.imgCollect.apply {
            setColorFilter(ContextCompat.getColor(ctx, R.color.ConstBrand))
            setImageResource(if (article.isCollect) R.drawable.ic_favorite_full else R.drawable.ic_favorite_border)
        }
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

        val tvAuthor: TextView
        val tvTime: TextView
        val imgPhoto: ImageView
        val tvTitle: TextView
        val tvChapter: TextView
        val tvTop: TextView
        val imgCollect: ImageView

        init {
            tvAuthor = itemView.findViewById(R.id.item_article_author)
            tvTime = itemView.findViewById(R.id.item_article_time)
            imgPhoto = itemView.findViewById(R.id.item_article_img)
            tvTitle = itemView.findViewById(R.id.item_article_title)
            tvChapter = itemView.findViewById(R.id.item_article_chapter)
            tvTop = itemView.findViewById(R.id.item_article_top)
            imgCollect = itemView.findViewById(R.id.item_article_collect)

            addOnClickListener(R.id.item_article_collect)
        }
    }
}