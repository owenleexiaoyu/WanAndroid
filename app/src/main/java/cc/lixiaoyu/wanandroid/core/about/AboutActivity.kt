package cc.lixiaoyu.wanandroid.core.about

import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.base.BaseSwipeBackActivity
import cc.lixiaoyu.wanandroid.databinding.ActivityAboutBinding
import cc.lixiaoyu.wanandroid.databinding.ActivityAboutBinding.*

class AboutActivity : BaseSwipeBackActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun initData() {}

    override fun initView() {
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.aboutToolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }
        // 初始化webview
        binding.aboutWebview.let {
            it.loadUrl("file:///android_asset/about_us.html")
            it.isHorizontalScrollBarEnabled = false
            it.isVerticalScrollBarEnabled = false
        }
    }

    override fun attachLayout(): Int {
        return R.layout.activity_about
    }
}