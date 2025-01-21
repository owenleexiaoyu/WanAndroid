package cc.lixiaoyu.wanandroid.core.project

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import cc.lixiaoyu.wanandroid.databinding.FragmentProjectBinding
import cc.lixiaoyu.wanandroid.core.project.model.Project
import cc.lixiaoyu.wanandroid.util.ToastUtil
import cc.lixiaoyu.wanandroid.util.behavior.IJumpToTop
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class ProjectFragment : Fragment(), IJumpToTop {

    private var _binding: FragmentProjectBinding? = null
    private val binding
        get() = _binding!!

    private var pagerAdapter: ProjectPagerAdapter? = null
    private val projectList: MutableList<Project> = mutableListOf()
    //当前加载的子fragment的序号
    private var currentChildFragmentIndex = 0

    private val apiService = RetrofitManager.wanAndroidService

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            try {
                val projects = apiService.getProjectList().data ?: emptyList()
                projectList.clear()
                projectList.addAll(projects)
                pagerAdapter?.notifyDataSetChanged()
            } catch (t: Throwable) {
                t.printStackTrace()
                ToastUtil.showToast("获取项目列表失败")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @SuppressLint("CheckResult")
    private fun initView() {
        pagerAdapter = ProjectPagerAdapter(this, projectList)
        binding.viewpager.apply {
            adapter = pagerAdapter
            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    currentChildFragmentIndex = position
                }
            })
        }
        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
            tab.text = projectList[position].name
        }.attach()
    }

    /**
     * 回到列表顶部
     */
    override fun jumpToListTop() {
        //将置顶功能进一步交给Adapter实现
        pagerAdapter?.jumpToListTop(currentChildFragmentIndex)
    }

    companion object {
        fun newInstance(): ProjectFragment {
            return ProjectFragment()
        }
    }
}