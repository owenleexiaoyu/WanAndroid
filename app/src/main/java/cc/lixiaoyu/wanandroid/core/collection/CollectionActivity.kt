package cc.lixiaoyu.wanandroid.core.collection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.databinding.ActivityCollectionBinding
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.coroutines.launch

class CollectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCollectionBinding
    private lateinit var viewModel: CollectionViewModel
    private lateinit var collectionAdapter: CollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[CollectionViewModel::class.java]
        collectionAdapter = CollectionAdapter(viewModel)
        setContentView(binding.root)
        initView()
        observeData()
    }

    private fun initView() {
        //设置返回按钮
        binding.collectionToolbar.title = getString(R.string.my_collections)
        setSupportActionBar(binding.collectionToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        // 设置RecyclerView的Adapter和布局管理器
        binding.collectionRecyclerview.apply {
            adapter = collectionAdapter
            layoutManager = LinearLayoutManager(this@CollectionActivity)
        }
        binding.collectionRefreshlayout.apply {
            // 设置SmartRefreshLayout的Header和Footer
            setRefreshHeader(MaterialHeader(this@CollectionActivity))
            setRefreshFooter(ClassicsFooter(this@CollectionActivity))
            // 设置下拉刷新和上拉加载更多的监听器
            setOnRefreshListener { viewModel.refreshCollectionArticleList() }
            setOnLoadMoreListener { viewModel.loadMoreCollectionArticleList() }
        }
        // 点击按钮回到列表顶部
        binding.collectionBtnUp.setOnClickListener {
            binding.collectionRecyclerview.smoothScrollToPosition(0)
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.articleList.collect {
                    collectionAdapter.setNewData(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isRefreshing.collect { isRefreshing ->
                    if (!isRefreshing) {
                        binding.collectionRefreshlayout.finishRefresh()
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoadingMore.collect { isLoadingMore ->
                    if (!isLoadingMore) {
                        binding.collectionRefreshlayout.finishLoadMore()
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "CollectionActivity"
    }
}