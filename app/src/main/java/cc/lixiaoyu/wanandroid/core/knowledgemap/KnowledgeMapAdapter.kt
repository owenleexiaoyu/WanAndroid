package cc.lixiaoyu.wanandroid.core.knowledgemap

import android.view.View
import android.widget.TextView
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.core.knowledgemap.model.KnowledgeNode
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class KnowledgeMapAdapter(layoutResId: Int, data: List<KnowledgeNode>) :
    BaseQuickAdapter<KnowledgeNode, KnowledgeMapAdapter.ViewHolder>(layoutResId, data) {

    override fun convert(helper: ViewHolder, item: KnowledgeNode) {
        helper.bind(item)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

        var tvTitle: TextView = itemView.findViewById(R.id.item_knowledge_category_title)

        var tvContent: TextView = itemView.findViewById(R.id.item_knowledge_category_content)

        fun bind(item: KnowledgeNode) {
            val sb = StringBuffer()
            tvTitle.text = item.name
            for (sc in item.children) {
                sb.append(sc.name + "   ")
            }
            tvContent.text = sb.toString().trim()
        }
    }
}