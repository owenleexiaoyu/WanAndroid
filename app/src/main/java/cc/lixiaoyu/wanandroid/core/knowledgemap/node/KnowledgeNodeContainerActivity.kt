package cc.lixiaoyu.wanandroid.core.knowledgemap.node

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import cc.lixiaoyu.wanandroid.core.knowledgemap.model.KnowledgeNode
import cc.lixiaoyu.wanandroid.core.knowledgemap.model.SubKnowledgeNode
import cc.lixiaoyu.wanandroid.databinding.ActivityKnowledgeNodeBinding
import com.google.android.material.tabs.TabLayoutMediator

class KnowledgeNodeContainerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKnowledgeNodeBinding
    private var knowledgeNode: KnowledgeNode? = null
    private var dataList: List<SubKnowledgeNode> = emptyList()

    private var mAdapter: SubKnowledgeNodePageAdapter? = null
    //当前加载的Fragment的序号
    private var mCurrentFragmentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKnowledgeNodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
    }

    private fun initData() {
        val intent = intent
        knowledgeNode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(EXTRA_KNOWLEDGE_NODE, KnowledgeNode::class.java)
        } else {
            intent.getSerializableExtra(EXTRA_KNOWLEDGE_NODE) as? KnowledgeNode
        }
        dataList = knowledgeNode?.children ?: emptyList()
    }

    private fun initView() {
        binding.toolbar.apply {
            title = knowledgeNode?.name ?: ""
            setSupportActionBar(this)
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        mAdapter = SubKnowledgeNodePageAdapter(this, dataList)
        binding.viewpager.adapter = mAdapter
        binding.viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                mCurrentFragmentIndex = position
            }
        })
        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
            tab.text = dataList[position].name
        }.attach()
        binding.btnUpToTop.setOnClickListener { mAdapter!!.jumpToListTop(mCurrentFragmentIndex) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private const val EXTRA_KNOWLEDGE_NODE = "knowledge_node"
        /**
         * 携带具体数据从其他Activity跳转到这个Activity，代替startActivity
         * @param context
         * @param knowledgeNode
         */
        fun actionStart(context: Context, knowledgeNode: KnowledgeNode) {
            val intent = Intent(context, KnowledgeNodeContainerActivity::class.java)
            intent.putExtra(EXTRA_KNOWLEDGE_NODE, knowledgeNode)
            context.startActivity(intent)
        }
    }
}