package cc.lixiaoyu.wanandroid.core.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.adapter.ArticleAdapter
import cc.lixiaoyu.wanandroid.core.detail.ArticleDetailActivity.Companion.actionStart
import cc.lixiaoyu.wanandroid.core.home.banner.BannerModel
import cc.lixiaoyu.wanandroid.core.home.banner.HomeBannerAdapter
import cc.lixiaoyu.wanandroid.databinding.FragmentHomeBinding
import cc.lixiaoyu.wanandroid.util.behavior.IJumpToTop
import cc.lixiaoyu.wanandroid.util.dp
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.youth.banner.Banner
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.indicator.Indicator
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), IJumpToTop {

    private var bannerView: Banner<BannerModel, HomeBannerAdapter>? = null
    private var bannerAdapter: HomeBannerAdapter? = null

    private var mAdapter: ArticleAdapter? = null

    private lateinit var viewModel: HomeViewModel

    private var _binding: FragmentHomeBinding? = null

    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private  fun initView() {
        //初始化Adapter
        mAdapter = ArticleAdapter(
            R.layout.item_recyclerview_article,
            ArrayList(0)
        )
        mAdapter!!.setOnItemClickListener { adapter, view, position ->
            val article = mAdapter!!.data[position]
            actionStart(requireActivity(), article.toDetailParam())
        }
        mAdapter!!.setOnItemChildClickListener { adapter, view, position ->
            viewModel.collectOrUnCollectArticle(position, mAdapter!!.data[position])
        }

        // 在RecyclerView中添加Banner的HeaderView
        initBanner()
        // 设置RecyclerView的Adapter和布局管理器
        binding.fhomeRecyclerview.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        binding.fhomeSmartRefresh.apply {
            // 设置SmartRefreshLayout的Header和Footer
            setRefreshHeader(MaterialHeader(activity))
            setRefreshFooter(ClassicsFooter(activity))
            // 设置下拉刷新和上拉加载更多的监听器
            setOnRefreshListener {
                viewModel.refreshHomeArticleList()
            }
            setOnLoadMoreListener {
                viewModel.loadMoreArticleList()
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bannerList.collect {
                    showBannerData(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.articleList.collect {
                    mAdapter?.setNewData(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isRefreshing.collect { isRefreshing ->
                    if (isRefreshing) {
                        binding.fhomeSmartRefresh.autoRefresh()
                    } else {
                        binding.fhomeSmartRefresh.finishRefresh()
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoadingMore.collect { isLoadingMore ->
                    if (isLoadingMore) {
                        binding.fhomeSmartRefresh.autoLoadMore()
                    } else {
                        binding.fhomeSmartRefresh.finishLoadMore()
                    }
                }
            }
        }
    }

    /**
     * 初始化Banner控件相关属性
     */
    private fun initBanner() {
        val bannerLayout = LayoutInflater
            .from(activity).inflate(R.layout.layout_banner, null) as ViewGroup
        bannerAdapter = HomeBannerAdapter(emptyList()).apply {
            setOnBannerListener { data, position ->
                val banner = data ?: return@setOnBannerListener
                actionStart(requireActivity(), banner.toDetailParam())
            }
        }
        bannerView = bannerLayout.findViewById<Banner<BannerModel, HomeBannerAdapter>>(R.id.fhome_banner).apply {
            setAdapter(bannerAdapter)
            indicator = CircleIndicator(this.context)
            setIndicatorSelectedColorRes(R.color.ConstBrand)
            setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
            addBannerLifecycleObserver(this@HomeFragment)
            setBannerRound(8.dp.toFloat())
        }
        bannerLayout.removeView(bannerView)
        mAdapter?.addHeaderView(bannerView)
    }

    private fun showBannerData(
        banners: List<BannerModel>
    ) {
        bannerAdapter?.setDatas(banners)
    }

    /**
     * 回到列表的顶部
     */
    override fun jumpToListTop() {
        binding.fhomeRecyclerview.smoothScrollToPosition(0)
    }

//    override fun onStart() {
//        super.onStart()
//        //开始自动轮播
//        bannerView?.startAutoPlay()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        //停止自动轮播
//        bannerView?.stopAutoPlay()
//    }


    companion object {
        private const val TAG = "HomeFragment"
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}