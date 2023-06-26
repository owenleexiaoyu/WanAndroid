package cc.lixiaoyu.wanandroid.core.nav

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.core.detail.ArticleDetailActivity
import cc.lixiaoyu.wanandroid.databinding.FragmentNavBinding
import cc.lixiaoyu.wanandroid.entity.Nav
import cc.lixiaoyu.wanandroid.entity.Nav.NavItem
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter

class NavFragment : Fragment() {

    private lateinit var binding: FragmentNavBinding
    private lateinit var navVM: NavViewModel
    private val mAdapter: NavAdapter by lazy { NavAdapter(R.layout.item_nav, listOf()) }

    private var navItemList: MutableList<NavItem> = mutableListOf()

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
        binding.fnavFlowlayout.apply {
            adapter = object : TagAdapter<Nav.NavItem>(navItemList) {
                override fun getView(parent: FlowLayout?, position: Int, t: NavItem?): View? {
                    val navItemTitle: TextView? = LayoutInflater.from(activity)
                        .inflate(R.layout.layout_nav_item, parent, false) as? TextView
                    navItemTitle?.text = t?.title
                    return navItemTitle
                }
            }
            setOnTagClickListener { _, position, _ ->
                ArticleDetailActivity.actionStart(activity, navItemList[position].toDetailParam())
                true
            }
        }
        subscribeData()
    }

    private fun subscribeData() {
        navVM.navList.observe(viewLifecycleOwner) {
            mAdapter.setNewData(it)
            refreshFlowLayout(it[navVM.currentPosition.value ?: 0].items)
        }
        navVM.currentPosition.observe(viewLifecycleOwner) {
            mAdapter.currentItem = it
            refreshFlowLayout(navVM.navList.value?.get(it)?.items ?: emptyList())
        }
    }

    private fun refreshFlowLayout(newList: List<NavItem>) {
        navItemList.clear()
        navItemList.addAll(newList)
        binding.fnavFlowlayout.onChanged()
    }

    companion object {
        fun newInstance(): NavFragment {
            return NavFragment()
        }
    }
}