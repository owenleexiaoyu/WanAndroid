package cc.lixiaoyu.wanandroid.core.todo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import cc.lixiaoyu.wanandroid.core.todo.ui.TodoFinishedFragment
import cc.lixiaoyu.wanandroid.core.todo.ui.TodoUnFinishedFragment

class TodoPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            TodoUnFinishedFragment()
        } else {
            TodoFinishedFragment()
        }
    }

}