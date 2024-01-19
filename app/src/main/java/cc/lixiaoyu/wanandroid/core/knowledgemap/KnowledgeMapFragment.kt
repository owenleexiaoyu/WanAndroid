package cc.lixiaoyu.wanandroid.core.knowledgemap

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
import cc.lixiaoyu.wanandroid.core.knowledgemap.model.KnowledgeNode
import cc.lixiaoyu.wanandroid.core.knowledgemap.node.KnowledgeNodeContainerActivity
import cc.lixiaoyu.wanandroid.databinding.FragmentKnowledgeMapBinding
import cc.lixiaoyu.wanandroid.util.behavior.IJumpToTop
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.coroutines.launch

class KnowledgeMapFragment : Fragment(), IJumpToTop {

    private lateinit var binding: FragmentKnowledgeMapBinding
    private lateinit var viewModel: KnowledgeMapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[KnowledgeMapViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKnowledgeMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {
        val knowledgeMapAdapter = KnowledgeMapAdapter(
            R.layout.item_knowledge_system_category, emptyList()
        ).apply {
            onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
                val knowledgeNode = data[position]
                goToKnowledgeNodePage(knowledgeNode)
            }
        }
        binding.knowledgeNodeList.apply {
            adapter = knowledgeMapAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.knowledgeNodeList.collect {
                    knowledgeMapAdapter.setNewData(it)
                }
            }
        }
    }

    private fun goToKnowledgeNodePage(knowledgeNode: KnowledgeNode) {
        activity?.let {
            KnowledgeNodeContainerActivity.actionStart(it, knowledgeNode)
        }
    }

    /**
     * 回到列表顶部
     */
    override fun jumpToListTop() {
        binding.knowledgeNodeList.smoothScrollToPosition(0)
    }

    companion object {
        fun newInstance(): KnowledgeMapFragment {
            return KnowledgeMapFragment()
        }
    }
}