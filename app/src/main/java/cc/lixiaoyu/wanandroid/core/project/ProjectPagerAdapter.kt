package cc.lixiaoyu.wanandroid.core.project

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import cc.lixiaoyu.wanandroid.core.projectdata.ProjectDataFragment
import cc.lixiaoyu.wanandroid.core.project.model.Project
import cc.lixiaoyu.wanandroid.util.behavior.IJumpToTop

class ProjectPagerAdapter(
    fragment: Fragment,
    private val dataList: List<Project>
) : FragmentStateAdapter(fragment) {

    private val fragmentList: MutableList<Fragment> = mutableListOf()

    fun jumpToListTop(index: Int) {
        (fragmentList[index] as IJumpToTop).jumpToListTop()
    }

    override fun getItemCount(): Int = dataList.size

    override fun createFragment(position: Int): Fragment {
        val fragment = ProjectDataFragment.newInstance(dataList[position])
        fragmentList.add(fragment)
        return fragment
    }
}