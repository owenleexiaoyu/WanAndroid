package cc.lixiaoyu.wanandroid.core.search.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.core.detail.ArticleDetailActivity.Companion.actionStart
import cc.lixiaoyu.wanandroid.databinding.ActivitySearchResultBinding
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.coroutines.launch

class SearchResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var viewModel: SearchResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        val keyword = intent.getStringExtra(EXTRA_SEARCH_KEYWORD) ?: ""
        val factory = SearchResultViewModelFactory(keyword)
        viewModel = ViewModelProvider(this, factory)[SearchResultViewModel::class.java]
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        // 设置返回按钮
        binding.toolbar.apply {
            title = "搜索结果：${viewModel.keyword}"
            setSupportActionBar(this)
        }
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }
        // 初始化 Adapter
        val searchResultAdapter = SearchResultAdapter(
            R.layout.item_recyclerview_search_result,
            emptyList()
        ).apply {
            setOnItemClickListener { _, _, position ->
                val article = data[position]
                actionStart(this@SearchResultActivity, article.toDetailParam())
            }
        }
        // 初始化 RecyclerView，设置 Adapter 和布局管理器
        binding.searchResultList.apply {
            adapter = searchResultAdapter
            layoutManager = LinearLayoutManager(this@SearchResultActivity)
        }
        // 设置 SmartRefreshLayout 的 Header 和 Footer
        binding.refreshlayout.apply {
            setRefreshHeader(MaterialHeader(this@SearchResultActivity))
            setRefreshFooter(ClassicsFooter(this@SearchResultActivity))
            setOnRefreshListener {
                viewModel.refreshSearchResult()
            }
            setOnLoadMoreListener {
                viewModel.loadMoreSearchResult()
            }
        }
        // 点击按钮回到列表顶部
        binding.btnUpToTop.setOnClickListener {
            binding.searchResultList.smoothScrollToPosition(0)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchResultList.collect {
                    searchResultAdapter.setNewData(it)
                    binding.refreshlayout.finishRefresh()
                    binding.refreshlayout.finishLoadMore()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val EXTRA_SEARCH_KEYWORD = "search_keyword"
        fun actionStart(context: Context, keyword: String) {
            val intent = Intent(context, SearchResultActivity::class.java)
            intent.putExtra(EXTRA_SEARCH_KEYWORD, keyword)
            context.startActivity(intent)
        }
    }
}