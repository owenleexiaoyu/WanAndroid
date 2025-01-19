package cc.lixiaoyu.wanandroid.core.search

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cc.lixiaoyu.wanandroid.adapter.FlowTagAdapter
import cc.lixiaoyu.wanandroid.core.detail.ArticleDetailActivity.Companion.actionStart
import cc.lixiaoyu.wanandroid.core.search.model.HotKey
import cc.lixiaoyu.wanandroid.core.search.model.WebSite
import cc.lixiaoyu.wanandroid.core.search.result.SearchResultActivity
import cc.lixiaoyu.wanandroid.databinding.ActivitySearchBinding
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        //设置返回按钮
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        //设置搜索按钮的点击事件
        binding.btnSearch.setOnClickListener {
            val word = binding.wordInput.text.toString().trim { it <= ' ' }
            if (word.isNotEmpty()) {
                // 搜索
                viewModel.searchKeyword(word)
                showSearchResult(word)
            }
            binding.wordInput.setText("")
        }
        //设置清空历史记录按钮的点击事件
        binding.btnClearHistory.setOnClickListener {
            showClearHistoryDialog()
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hotKeyList.collect {
                    val hotKeyAdapter = FlowTagAdapter<HotKey>().apply {
                        setTitleConverter { data -> data.name }
                        setOnTagClickListener { view, position, data ->
                            val hotKeyName = data.name
                            viewModel.searchKeyword(hotKeyName)
                            showSearchResult(hotKeyName)
                        }
                    }
                    binding.tfHotkeys.apply {
                        adapter = hotKeyAdapter
                        layoutManager = FlexboxLayoutManager(this.context)
                    }
                    hotKeyAdapter.setData(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.commonSiteList.collect {
                    val siteNameAdapter = FlowTagAdapter<WebSite>().apply {
                        setTitleConverter { data -> data.name }
                        setOnTagClickListener { view, position, data ->
                            actionStart(this@SearchActivity, data.toDetailParam())
                        }
                    }
                    binding.tfCommonsite.apply {
                        adapter = siteNameAdapter
                        layoutManager = FlexboxLayoutManager(this.context)
                    }
                    siteNameAdapter.setData(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchHistoryList.collect {
                    val searchHistoryAdapter = FlowTagAdapter<String>().apply {
                        setTitleConverter { data -> data }
                        setOnTagClickListener { view, position, data ->
                            showSearchResult(data)
                        }
                    }
                    binding.tfHistory.apply {
                        adapter = searchHistoryAdapter
                        layoutManager = FlexboxLayoutManager(this.context)
                    }

                    searchHistoryAdapter.setData(it)
                }
            }
        }
    }

    /**
     * 显示清空历史记录的弹窗
     */
    private fun showClearHistoryDialog() {
        val builder = AlertDialog.Builder(this@SearchActivity)
        val dialog = builder.setTitle("提示")
            .setMessage("确认清空历史记录吗")
            .setCancelable(true)
            .setPositiveButton("确定") { _, _ -> viewModel.clearSearchHistory() }
            .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
            .create()
        dialog.show()
    }

    private fun showSearchResult(keyword: String) {
        //打开搜索结果页
        SearchResultActivity.actionStart(this, keyword)
        //关闭当前页面
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}