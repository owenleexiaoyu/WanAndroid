package cc.lixiaoyu.wanandroid.core.search.result

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.entity.Article
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SearchResultAdapter(layoutResId: Int, data: List<Article>) :
    BaseQuickAdapter<Article, SearchResultAdapter.ViewHolder>(layoutResId, data) {

    override fun convert(holder: ViewHolder, article: Article) {
        holder.bind(article)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val tvAuthor: TextView = itemView.findViewById(R.id.item_sresult_author)
        val tvTitle: TextView = itemView.findViewById(R.id.item_sresult_title)

        fun bind(article: Article) {
            tvAuthor.text = article.author
            //处理关键词高亮
            var title = article.title
            val start = title.indexOf("<em class='highlight'>")
            title = title.replace("<em class='highlight'>", "")
            val end = title.indexOf("</em>")
            title = title.replace("</em>", "")
            val ss = SpannableString(title)
            if (start in 0 until end && end <= title.length) {
                ss.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.ConstHighlight
                        )
                    ),
                    start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            tvTitle.text = ss
        }
    }
}