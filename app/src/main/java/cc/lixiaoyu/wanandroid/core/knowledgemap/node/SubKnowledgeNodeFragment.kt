package cc.lixiaoyu.wanandroid.core.knowledgemap.node

import android.os.Build
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
import cc.lixiaoyu.wanandroid.core.knowledgemap.model.SubKnowledgeNode
import cc.lixiaoyu.wanandroid.databinding.FragmentSubKnowledgenodeArticleBinding
import cc.lixiaoyu.wanandroid.util.behavior.IJumpToTop
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.footer.ClassicsFooter
import kotlinx.coroutines.launch

class SubKnowledgeNodeFragment : Fragment(), IJumpToTop {

    private lateinit var viewModel: SubKnowledgeNodeViewModel

    private var _binding: FragmentSubKnowledgenodeArticleBinding? = null
    private val binding
        get() = _binding!!

    private var subKnowledgeNode: SubKnowledgeNode? = null

    private var articleAdapter: ArticleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subKnowledgeNode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(ARGUMENTS_KEY, SubKnowledgeNode::class.java)
            } else {
                it.getSerializable(ARGUMENTS_KEY) as? SubKnowledgeNode
            }
        }
        val factory = SubKnowledgeNodeViewModelFactory(subKnowledgeNode?.id.toString())
        viewModel = ViewModelProvider(this, factory)[SubKnowledgeNodeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubKnowledgenodeArticleBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        articleAdapter = ArticleAdapter(
            R.layout.item_recyclerview_article,
            ArrayList(0)
        )
        articleAdapter!!.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view1: View?, position: Int ->
                val article = articleAdapter!!.data[position]
                actionStart(requireActivity(), article.toDetailParam())
            }
        articleAdapter!!.onItemChildClickListener =
            BaseQuickAdapter.OnItemChildClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
                //点击收藏按钮
                viewModel.collectOrUnCollectArticle(requireContext(), position, articleAdapter!!.data[position])
            }
        binding.articleList.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        binding.refreshLayout.apply {
            setRefreshHeader(MaterialHeader(activity))
            setRefreshFooter(ClassicsFooter(activity))
            setOnRefreshListener { //强制刷新数据
                viewModel.refreshArticleListByCid()
            }
            setOnLoadMoreListener { refreshLayout: RefreshLayout? ->
                viewModel.loadMoreArticleListByCid()
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.articleList.collect {
                    articleAdapter?.setNewData(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isRefreshing.collect { isRefreshing ->
                    if (isRefreshing) {
                        binding.refreshLayout.autoRefresh()
                    } else {
                        binding.refreshLayout.finishRefresh()
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoadingMore.collect { isLoadingMore ->
                    if (isLoadingMore) {
                        binding.refreshLayout.autoLoadMore()
                    } else {
                        binding.refreshLayout.finishLoadMore()
                    }
                }
            }
        }
    }

    /**
     * 回到列表顶部
     */
    override fun jumpToListTop() {
        binding.articleList.smoothScrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARGUMENTS_KEY = "sub_knowledge_node"

        fun newInstance(subKnowledgeNode: SubKnowledgeNode): SubKnowledgeNodeFragment {
            val bundle = Bundle()
            val fragment = SubKnowledgeNodeFragment()
            bundle.putSerializable(ARGUMENTS_KEY, subKnowledgeNode)
            fragment.arguments = bundle
            return fragment
        }
    }
}