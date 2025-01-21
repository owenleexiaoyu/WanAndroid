package cc.lixiaoyu.wanandroid.core.wechat

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import cc.lixiaoyu.wanandroid.api.WanAndroidService
import cc.lixiaoyu.wanandroid.core.wechat.model.WeChatOfficialAccount
import cc.lixiaoyu.wanandroid.databinding.FragmentWechatBinding
import cc.lixiaoyu.wanandroid.util.ToastUtil
import cc.lixiaoyu.wanandroid.util.behavior.IJumpToTop
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class WeChatFragment : Fragment(), IJumpToTop {

    private var _binding: FragmentWechatBinding? = null
    private val binding
        get() = _binding!!

    private var pagerAdapter: WeChatPagerAdapter? = null
    private val apiService: WanAndroidService by lazy { RetrofitManager.wanAndroidService }
    private var dataList: MutableList<WeChatOfficialAccount> = mutableListOf()

    // 当前加载的子fragment的序号
    private var currentChildFragmentIndex = 0

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            try {
                val officialAccounts = apiService.getWetChatOfficialAccountList().data ?: emptyList()
                dataList.clear()
                dataList.addAll(officialAccounts)
                pagerAdapter?.notifyDataSetChanged()
            } catch (t: Throwable) {
                t.printStackTrace()
                ToastUtil.showToast("获取微信公众号列表失败")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWechatBinding.inflate(inflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        pagerAdapter = WeChatPagerAdapter(this, dataList)
        binding.viewpager.apply {
            adapter = pagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    currentChildFragmentIndex = position
                }
            })
        }
        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
            tab.text = dataList[position].name
        }.attach()
    }


    /**
     * 回到列表顶部
     */
    override fun jumpToListTop() {
        // 将置顶功能进一步交给Adapter实现
        pagerAdapter?.jumpToListTop(currentChildFragmentIndex)
    }

    companion object {
        fun newInstance(): WeChatFragment {
            return WeChatFragment()
        }
    }
}