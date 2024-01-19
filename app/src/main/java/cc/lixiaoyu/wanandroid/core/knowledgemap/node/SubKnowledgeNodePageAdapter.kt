package cc.lixiaoyu.wanandroid.core.knowledgemap.node

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import cc.lixiaoyu.wanandroid.core.knowledgemap.model.SubKnowledgeNode
import cc.lixiaoyu.wanandroid.util.behavior.IJumpToTop

class SubKnowledgeNodePageAdapter(
    fragmentActivity: FragmentActivity,
    private val subKnowledgeNodeList: List<SubKnowledgeNode>
) : FragmentStateAdapter(fragmentActivity) {

    private val mFragmentList: MutableList<Fragment> = ArrayList(subKnowledgeNodeList.size)

    fun jumpToListTop(index: Int) {
        (mFragmentList[index] as? IJumpToTop)?.jumpToListTop()
    }

    override fun getItemCount(): Int {
        return subKnowledgeNodeList.size
    }

    override fun createFragment(position: Int): Fragment {
        val subClassFragment = SubKnowledgeNodeFragment.newInstance(
            subKnowledgeNodeList[position]
        )
        mFragmentList.add(subClassFragment)
        return subClassFragment
    }
}