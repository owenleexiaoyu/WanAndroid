package cc.lixiaoyu.wanandroid.core.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.lixiaoyu.wanandroid.databinding.ActivityAboutBinding
import cc.lixiaoyu.wanandroid.databinding.ActivityAboutBinding.*

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        setSupportActionBar(binding.aboutToolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }
        // 初始化 webview
        binding.aboutWebview.let {
            it.loadUrl("file:///android_asset/about_us.html")
            it.isHorizontalScrollBarEnabled = false
            it.isVerticalScrollBarEnabled = false
        }
    }

}