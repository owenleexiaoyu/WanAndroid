package cc.lixiaoyu.wanandroid.core.knowledgemap.node

import android.content.Intent
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
import cc.lixiaoyu.wanandroid.core.account.ui.LoginActivity
import cc.lixiaoyu.wanandroid.core.detail.ArticleDetailActivity.Companion.actionStart
import cc.lixiaoyu.wanandroid.core.knowledgemap.model.SubKnowledgeNode
import cc.lixiaoyu.wanandroid.databinding.FragmentSubKnowledgenodeArticleBinding
import cc.lixiaoyu.wanandroid.entity.Article
import cc.lixiaoyu.wanandroid.util.behavior.IJumpToTop
import cc.lixiaoyu.wanandroid.util.storage.DataManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.coroutines.launch

class SubKnowledgeNodeFragment : Fragment(), IJumpToTop {

    private var _binding: FragmentSubKnowledgenodeArticleBinding? = null
    private lateinit var viewModel: SubKnowledgeNodeViewModel

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
                openArticleDetail(article)
            }
        articleAdapter!!.onItemChildClickListener =
            BaseQuickAdapter.OnItemChildClickListener { adapter: BaseQuickAdapter<*, *>?, view12: View?, position: Int ->
                //点击收藏按钮
                //判断是否用户登陆
                if (DataManager.loginState) {
                    //未登录，前往登陆页面进行登陆操作
                    startActivity(Intent(activity, LoginActivity::class.java))
                } else {
                    //登陆后，可以进行文章的收藏与取消收藏操作
                    //如果文章已经被收藏了，就取消收藏，如果没有收藏，就收藏
                    if (articleAdapter!!.getItem(position)!!.isCollect) {
                        // cancelCollectArticle(position, mAdapter!!.getItem(position))
                    } else {
                        // collectArticle(position, mAdapter!!.getItem(position))
                    }
                }
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
    }


    private fun openArticleDetail(article: Article) {
        actionStart(requireActivity(), article.toDetailParam())
    }

//    override fun showCollectArticle(success: Boolean, position: Int) {
//        if (success) {
//            ToastUtil.showToast("收藏文章成功")
//            mAdapter!!.data[position].isCollect = true
//            mAdapter!!.notifyDataSetChanged()
//        } else {
//            ToastUtil.showToast("收藏文章失败")
//        }
//    }
//
//    override fun showCancelCollectArticle(success: Boolean, position: Int) {
//        if (success) {
//            ToastUtil.showToast("取消收藏文章成功")
//            mAdapter!!.data[position].isCollect = false
//            mAdapter!!.notifyDataSetChanged()
//        } else {
//            ToastUtil.showToast("取消收藏文章失败")
//        }
//    }

//    override fun showLoadMoreArticleByCid(articles: List<Article>, success: Boolean) {
//        //如果成功
//        if (success) {
//            mAdapter!!.addData(articles)
//            mRefreshLayout!!.finishLoadMore()
//        } else {
//            mRefreshLayout!!.finishLoadMore(false)
//        }
//    }

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