package cc.lixiaoyu.wanandroid.core.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.base.BaseFragment

/**
 * 我的积分页面
 */
class MyIntegralFragment : BaseFragment() {

    private var headerView : View? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeData()
    }

    private fun subscribeData() {

    }

    override fun initData() {
    }

    override fun initView(view: View?) {
        headerView = LayoutInflater.from(this.activity).inflate(R.layout.layout_my_integral_header, null)

    }

    override fun attachLayout(): Int = R.layout.fragment_my_integral
}