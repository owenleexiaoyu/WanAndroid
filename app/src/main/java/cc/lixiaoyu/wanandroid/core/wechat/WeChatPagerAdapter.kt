package cc.lixiaoyu.wanandroid.core.wechat

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import cc.lixiaoyu.wanandroid.core.wechat.model.WeChatOfficialAccount
import cc.lixiaoyu.wanandroid.core.wechat.officialaccount.WeChatOfficialAccountFragment
import cc.lixiaoyu.wanandroid.util.behavior.IJumpToTop

class WeChatPagerAdapter(
    fragment: Fragment,
    private val dataList: List<WeChatOfficialAccount>
) : FragmentStateAdapter(fragment) {

    private val fragmentList: MutableList<Fragment> = mutableListOf()

    fun jumpToListTop(index: Int) {
        (fragmentList[index] as IJumpToTop).jumpToListTop()
    }

    override fun getItemCount(): Int = dataList.size

    override fun createFragment(position: Int): Fragment {
        val fragment = WeChatOfficialAccountFragment.newInstance(dataList[position])
        fragmentList.add(fragment)
        return fragment
    }
}