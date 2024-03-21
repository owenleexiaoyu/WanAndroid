package cc.lixiaoyu.wanandroid.core.project.category

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
import cc.lixiaoyu.wanandroid.core.project.model.Project
import cc.lixiaoyu.wanandroid.databinding.FragmentProjectDataBinding
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.coroutines.launch

class CategoryProjectFragment : Fragment() {

    private var _binding: FragmentProjectDataBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var viewModel: CategoryProjectViewModel
    private lateinit var dataAdapter: CategoryProjectAdapter

    private var project: Project? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val project = arguments?.getSerializable(ARGUMENTS_KEY) as? Project
        viewModel = ViewModelProvider(this, CategoryProjectViewModelFactory(project?.id.toString()))[CategoryProjectViewModel::class.java]
        dataAdapter = CategoryProjectAdapter(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectDataBinding.inflate(inflater)
        return _binding!!.root
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
        binding.fprojectDataRecyclerview.apply {
            adapter = dataAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        binding.fprojectDataSmartrefreshlayout.apply {
            setRefreshHeader(MaterialHeader(activity))
            setRefreshFooter(ClassicsFooter(activity))
            setOnRefreshListener { //强制刷新数据
                viewModel.refreshCategoryProjectList()
            }
            setOnLoadMoreListener {
                viewModel.loadMoreCategoryProjectList()
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.articleList.collect {
                    dataAdapter.setNewData(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isRefreshing.collect { isRefreshing ->
                    if (isRefreshing) {
                        binding.fprojectDataSmartrefreshlayout.autoRefresh()
                    } else {
                        binding.fprojectDataSmartrefreshlayout.finishRefresh()
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoadingMore.collect { isLoadingMore ->
                    if (isLoadingMore) {
                        binding.fprojectDataSmartrefreshlayout.autoLoadMore()
                    } else {
                        binding.fprojectDataSmartrefreshlayout.finishLoadMore()
                    }
                }
            }
        }
    }


//    override fun showCollectArticle(success: Boolean, position: Int) {
//        if (success) {
//            showToast("收藏文章成功")
//            mAdapter!!.data[position].isCollect = true
//            mAdapter!!.notifyDataSetChanged()
//        } else {
//            showToast("收藏文章失败")
//        }
//    }
//
//    override fun showCancelCollectArticle(success: Boolean, position: Int) {
//        if (success) {
//            showToast("取消收藏文章成功")
//            mAdapter!!.data[position].isCollect = false
//            mAdapter!!.notifyDataSetChanged()
//        } else {
//            showToast("取消收藏文章失败")
//        }
//    }

//    /**
//     * 回到列表顶部
//     */
//    fun jumpToListTop() {
//        mRecyclerView!!.smoothScrollToPosition(0)
//    }

    companion object {
        private const val ARGUMENTS_KEY = "project_title"
        fun newInstance(title: Project?): CategoryProjectFragment {
            val bundle = Bundle()
            bundle.putSerializable(ARGUMENTS_KEY, title)
            val fragment = CategoryProjectFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}