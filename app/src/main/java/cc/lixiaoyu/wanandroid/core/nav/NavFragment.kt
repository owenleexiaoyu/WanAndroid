package cc.lixiaoyu.wanandroid.core.nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.adapter.FlowTagAdapter
import cc.lixiaoyu.wanandroid.core.detail.ArticleDetailActivity
import cc.lixiaoyu.wanandroid.databinding.FragmentNavBinding
import com.google.android.flexbox.FlexboxLayoutManager

class NavFragment : Fragment() {

    private lateinit var binding: FragmentNavBinding
    private lateinit var navVM: NavViewModel
    private val mAdapter: NavAdapter by lazy { NavAdapter(R.layout.item_nav, listOf()) }
    private val navItemAdapter: FlowTagAdapter<NavItem> by lazy {
        FlowTagAdapter<NavItem>().apply {
            setTitleConverter { it.title }
            setOnTagClickListener { view, position, data ->
                ArticleDetailActivity.actionStart(view.context, data.toDetailParam())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNavBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navVM = ViewModelProvider(this)[NavViewModel::class.java]
        mAdapter.setOnItemClickListener { _, _, position ->
            navVM.changeCurrentPosition(position)
        }
        binding.fnavRecyclerview.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        binding.fnavFlowLayoutContainer.apply {
            adapter = navItemAdapter
            layoutManager = FlexboxLayoutManager(this.context)
//            setOnTagClickListener { _, position, _ ->
//                ArticleDetailActivity.actionStart(context, navItemList[position].toDetailParam())
//                true
//            }
        }
        subscribeData()
    }

    private fun subscribeData() {
        navVM.navTitleList.observe(viewLifecycleOwner) {
            mAdapter.setNewData(it)
        }
        navVM.getCurrentPosition().observe(viewLifecycleOwner) {
            mAdapter.currentItem = it
        }
        navVM.currentNavItemList.observe(viewLifecycleOwner) {
            refreshFlowLayout(it)
        }
    }

    private fun refreshFlowLayout(newList: List<NavItem>) {
        navItemAdapter.setData(newList)
    }

    companion object {
        fun newInstance(): NavFragment {
            return NavFragment()
        }
    }
}