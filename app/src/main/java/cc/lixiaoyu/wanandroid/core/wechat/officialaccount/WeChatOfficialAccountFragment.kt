package cc.lixiaoyu.wanandroid.core.wechat.officialaccount

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
import cc.lixiaoyu.wanandroid.core.detail.ArticleDetailActivity
import cc.lixiaoyu.wanandroid.core.wechat.model.WeChatOfficialAccount
import cc.lixiaoyu.wanandroid.databinding.FragmentWechatDataBinding
import cc.lixiaoyu.wanandroid.entity.Article
import cc.lixiaoyu.wanandroid.util.behavior.IJumpToTop
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.footer.ClassicsFooter
import kotlinx.coroutines.launch

class WeChatOfficialAccountFragment : Fragment(), IJumpToTop {

    private var officialAccount: WeChatOfficialAccount? = null
    private lateinit var viewModel: WeChatOfficialAccountViewModel

    private var _binding: FragmentWechatDataBinding? = null
    private val binding
        get() = _binding!!

    private var listAdapter: ArticleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        officialAccount = arguments?.getSerializable(ARGUMENTS_KEY) as? WeChatOfficialAccount
        viewModel = ViewModelProvider(this, WeChatOfficialAccountViewModelFactory(officialAccount?.id.toString()))[WeChatOfficialAccountViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWechatDataBinding.inflate(inflater)
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

    private fun initView() {
        listAdapter = ArticleAdapter(
            R.layout.item_recyclerview_article,
            ArrayList<Article>(0)
        )
        listAdapter?.apply {
            setOnItemClickListener { adapter, view, position ->
                val article: Article = data[position]
                ArticleDetailActivity.actionStart(view.context, article.toDetailParam())
            }
            setOnItemChildClickListener { adapter, view, position ->
                //点击收藏按钮
                viewModel.collectOrUnCollectArticleInArticleList(position, data[position])
            }
        }
        binding.fwechatDataRecyclerview.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(getActivity())
        }
        binding.fwechatDataSmartrefreshlayout.apply {
            setRefreshHeader(MaterialHeader(activity))
            setRefreshFooter(ClassicsFooter(activity))
            setOnRefreshListener { // 强制刷新数据
                viewModel.refreshWeChatOfficialAccountArticleList()
            }
            setOnLoadMoreListener {
                viewModel.loadMoreWeChatOfficialAccountArticleList()
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.articleList.collect {
                    listAdapter?.setNewData(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isRefreshing.collect { isRefreshing ->
                    if (isRefreshing) {
                        binding.fwechatDataSmartrefreshlayout.autoRefresh()
                    } else {
                        binding.fwechatDataSmartrefreshlayout.finishRefresh()
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoadingMore.collect { isLoadingMore ->
                    if (isLoadingMore) {
                        binding.fwechatDataSmartrefreshlayout.autoLoadMore()
                    } else {
                        binding.fwechatDataSmartrefreshlayout.finishLoadMore()
                    }
                }
            }
        }
    }


    /**
     * 回到列表顶部
     */
    override fun jumpToListTop() {
        binding.fwechatDataRecyclerview.smoothScrollToPosition(0)
    }

    companion object {
        private const val ARGUMENTS_KEY = "wechat_officialaccount"
        fun newInstance(title: WeChatOfficialAccount): WeChatOfficialAccountFragment {
            val bundle = Bundle()
            bundle.putSerializable(ARGUMENTS_KEY, title)
            val fragment = WeChatOfficialAccountFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}