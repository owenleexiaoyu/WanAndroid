package cc.lixiaoyu.wanandroid.core.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.core.detail.more.DetailMoreSheet.Companion.newInstance
import cc.lixiaoyu.wanandroid.databinding.ActivityDetailBinding
import cc.lixiaoyu.wanandroid.util.RxBus
import cc.lixiaoyu.wanandroid.util.ToastUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * 文章详情页
 */
class ArticleDetailActivity : AppCompatActivity() {

    private var detailParam: DetailParam? = null
    private lateinit var binding: ActivityDetailBinding
    private var dispose: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        subscribeReloadEvent()
    }

    private fun subscribeReloadEvent() {
        dispose = RxBus.getInstance().toFlowable(ReloadArticleDetailEvent::class.java)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.detailWebview.reload()
            }
    }

    private fun initData() {
        detailParam = intent.getSerializableExtra(ARTICLE_DETAIL_PARAM) as? DetailParam
        if (detailParam == null) {
            ToastUtil.showToast("文章参数为空")
            finish()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        binding.detailToolbarTitle.apply {
            text = Html.fromHtml(detailParam?.title ?: "")
            isSelected = true
        }
        binding.detailProgressbar.apply {
            max = 100
            setIndicatorColor(ContextCompat.getColor(this@ArticleDetailActivity, R.color.Accent))
        }
        setSupportActionBar(binding.detailToolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }
        binding.detailWebview.apply {
            settings.javaScriptEnabled = true
            webChromeClient = DetailWebChromeClient()
            webViewClient = DetailWebViewClient()
            loadUrl(detailParam!!.link)
        }
    }

    internal inner class DetailWebChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            binding.detailProgressbar.setProgressCompat(newProgress, true)
        }
    }

    /**
     * 自定义 WebViewClient，在网页开始加载时显示进度条，加载完成后隐藏进度条
     */
    internal inner class DetailWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            binding.detailProgressbar.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            binding.detailProgressbar.visibility = View.GONE
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val uri = request?.url
            return !("http" == uri?.scheme || "https" == uri?.scheme)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail_more, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_detail_more -> {
                val sheet = newInstance(detailParam!!)
                sheet.show(supportFragmentManager, "detail_more_sheet")
            }
            android.R.id.home -> finish()
            else -> {}
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose?.dispose()
    }

    companion object {
        private const val ARTICLE_DETAIL_PARAM = "article_detail_param"
        /**
         * 从其他Activity跳转到本Activity，代替startActivity
         *
         * @param context
         * @param detailParam
         */
        @JvmStatic
        fun actionStart(context: Context, detailParam: DetailParam) {
            val intent = Intent(context, ArticleDetailActivity::class.java)
            intent.putExtra(ARTICLE_DETAIL_PARAM, detailParam)
            context.startActivity(intent)
        }
    }
}