package cc.lixiaoyu.wanandroid.core.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cc.lixiaoyu.amz_uikit.item.AMZItemView
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.util.ToastUtil

/**
 * 个人主页
 */
class ProfileFragment : Fragment(), View.OnClickListener{

    // 我的积分
    private var myIntegral: AMZItemView? = null
    // 积分排行
    private var integralRate: AMZItemView? = null

    // 我的分享
    private var myPosts: AMZItemView? = null
    // 我的收藏
    private var myCollections: AMZItemView? = null
    // 浏览历史
    private var myHistories: AMZItemView? = null

    // 开源许可
    private var openSourceLicense: AMZItemView? = null
    // 关于作者
    private var aboutAuthor: AMZItemView? = null
    // 系统设置
    private var appSettings: AMZItemView? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        myIntegral = view.findViewById(R.id.my_integral)
        integralRate = view.findViewById(R.id.integral_rate)
        myPosts = view.findViewById(R.id.my_posts)
        myCollections = view.findViewById(R.id.my_collections)
        myHistories = view.findViewById(R.id.my_histories)
        openSourceLicense = view.findViewById(R.id.opensource_license)
        aboutAuthor = view.findViewById(R.id.about_author)
        appSettings = view.findViewById(R.id.app_settings)

        myIntegral?.setOnClickListener(this)
        integralRate?.setOnClickListener(this)
        myPosts?.setOnClickListener(this)
        myCollections?.setOnClickListener(this)
        myHistories?.setOnClickListener(this)
        openSourceLicense?.setOnClickListener(this)
        aboutAuthor?.setOnClickListener(this)
        appSettings?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.my_integral -> clickMyIntegral(v)
            R.id.integral_rate -> clickIntegralRate(v)
        }
    }

    private fun clickMyIntegral(v: View) {
        ToastUtil.showToast("我的积分")
    }

    private fun clickIntegralRate(v: View) {
        ToastUtil.showToast("积分排行")
    }
}