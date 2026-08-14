package cc.lixiaoyu.wanandroid.core.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.core.home.HomeFragment
import cc.lixiaoyu.wanandroid.core.main.drawer.DrawerFragment
import cc.lixiaoyu.wanandroid.core.detail.ArticleDetailActivity
import cc.lixiaoyu.wanandroid.core.detail.DetailParam
import cc.lixiaoyu.wanandroid.kmp.discover.android.DiscoverContainerFragment
import cc.lixiaoyu.wanandroid.kmp.discover.android.DiscoverWebDetailOpener
import cc.lixiaoyu.wanandroid.kmp.nav.android.NavContainerFragment
import cc.lixiaoyu.wanandroid.kmp.nav.android.NavWebDetailOpener
import cc.lixiaoyu.wanandroid.kmp.mine.android.MineActionHandler
import cc.lixiaoyu.wanandroid.kmp.mine.android.MineContainerFragment
import cc.lixiaoyu.wanandroid.kmp.mine.ui.MineAction
import cc.lixiaoyu.wanandroid.core.search.SearchActivity
import cc.lixiaoyu.wanandroid.core.knowledgemap.KnowledgeMapFragment
import cc.lixiaoyu.wanandroid.core.settings.KmpSettingsActivity
import cc.lixiaoyu.wanandroid.databinding.ActivityMainBinding
import cc.lixiaoyu.wanandroid.util.behavior.IJumpToTop
import cc.lixiaoyu.wanandroid.util.ToastUtil
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem

class MainActivity : AppCompatActivity(), NavWebDetailOpener, DiscoverWebDetailOpener, MineActionHandler {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainVM: MainViewModel

    private var homeFragment: Fragment? = null
    private var knowledgeFragment: Fragment? = null
    private var discoverFragment: Fragment? = null
    private var navFragment: Fragment? = null
    private var mineFragment: Fragment? = null
    private val fragmentMap: MutableMap<String, Fragment> = mutableMapOf()

    private var drawerFragment: Fragment? = null

    private val titleResIds = intArrayOf(
        R.string.home_page,
        R.string.knowledge_system,
        R.string.discover,
        R.string.navigation,
        R.string.mine
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainVM = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        subscribeObserver()
    }

    private fun subscribeObserver() {
        mainVM.currentTabIndex.observe(this) { index ->
            // 改变 ToolBar Title
            binding.mainPage.mainToolbar.title = getString(titleResIds[index])
            // 隐藏或显示回到顶部按钮
            binding.mainPage.mainBtnUp.visibility = if (index == 2 || index == 3 || index == 4) View.GONE else View.VISIBLE
            // 切换 Fragment
            loadFragment(index)
        }
    }

    /**
     * 初始化控件
     */
    fun initView() {
        setSupportActionBar(binding.mainPage.mainToolbar)
        initBottomNavBar()
        initDrawer()

        //处理向上置顶按钮的事件
        binding.mainPage.mainBtnUp.setOnClickListener {
            when (mainVM.currentTabIndex.value ?: 0) {
                0 -> (fragmentMap[HOME_FRAGMENT_TAG] as? IJumpToTop)?.jumpToListTop()
                1 -> (fragmentMap[KNOWLEDGE_FRAGMENT_TAG] as? IJumpToTop)?.jumpToListTop()
                2 -> Unit
                4 -> Unit
            }
        }
    }

    /**
     * 初始化底部导航栏
     */
    private fun initBottomNavBar() {
        binding.mainPage.mainBottomNavBar.apply {
            setMode(BottomNavigationBar.MODE_FIXED)
            setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
            addItem(
                BottomNavigationItem(R.drawable.ic_home, getString(titleResIds[0]))
                    .setActiveColor(ContextCompat.getColor(context, R.color.ConstBrand))
            )
            addItem(
                BottomNavigationItem(R.drawable.ic_knowledge_system, getString(titleResIds[1]))
                    .setActiveColor(ContextCompat.getColor(context, R.color.ConstBrand))
            )
            addItem(
                BottomNavigationItem(R.drawable.ic_project, getString(titleResIds[2]))
                    .setActiveColor(ContextCompat.getColor(context, R.color.ConstBrand))
            )
            addItem(
                BottomNavigationItem(R.drawable.ic_navigation, getString(titleResIds[3]))
                    .setActiveColor(ContextCompat.getColor(context, R.color.ConstBrand))
            )
            addItem(
                BottomNavigationItem(R.drawable.ic_person_black_24dp, getString(titleResIds[4]))
                    .setActiveColor(ContextCompat.getColor(context, R.color.ConstBrand))
            )
            setFirstSelectedPosition(mainVM.currentTabIndex.value ?: 0)
            setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
                override fun onTabSelected(position: Int) {
                    mainVM.changeCurrentIndex(position)
                }
                override fun onTabUnselected(position: Int) {}
                override fun onTabReselected(position: Int) {}
            })
            initialise()
        }
    }

    /**
     * 加载Fragment
     *
     * @param index
     */
    private fun loadFragment(index: Int) {
        when (index) {
            0 -> {
                homeFragment = supportFragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG)
                if (homeFragment == null) {
                    homeFragment = HomeFragment.newInstance()
                }
                addAndShowFragment(requireNotNull(homeFragment), HOME_FRAGMENT_TAG)
            }
            1 -> {
                knowledgeFragment = supportFragmentManager.findFragmentByTag(KNOWLEDGE_FRAGMENT_TAG)
                if (knowledgeFragment == null) {
                    knowledgeFragment = KnowledgeMapFragment.newInstance()
                }
                addAndShowFragment(requireNotNull(knowledgeFragment), KNOWLEDGE_FRAGMENT_TAG)
            }
            2 -> {
                discoverFragment = supportFragmentManager.findFragmentByTag(DISCOVER_FRAGMENT_TAG)
                if (discoverFragment == null) {
                    discoverFragment = DiscoverContainerFragment.newInstance()
                }
                addAndShowFragment(requireNotNull(discoverFragment), DISCOVER_FRAGMENT_TAG)
            }
            3 -> {
                navFragment = supportFragmentManager.findFragmentByTag(NAV_FRAGMENT_TAG)
                if (navFragment == null) {
                    navFragment = NavContainerFragment.newInstance()
                }
                addAndShowFragment(requireNotNull(navFragment), NAV_FRAGMENT_TAG)
            }
            4 -> {
                mineFragment = supportFragmentManager.findFragmentByTag(MINE_FRAGMENT_TAG)
                if (mineFragment == null) {
                    mineFragment = MineContainerFragment.newInstance()
                }
                addAndShowFragment(requireNotNull(mineFragment), MINE_FRAGMENT_TAG)
            }
            else -> throw IllegalArgumentException("Index [$index] is not support")
        }
    }

    private fun addAndShowFragment(fragment: Fragment, tag: String) {
        fragmentMap[tag] = fragment
        if (!fragment.isAdded) {
            supportFragmentManager.beginTransaction().add(R.id.main_container, fragment, tag).commit()
        }
        for (frag in fragmentMap.values) {
            if (frag !== fragment) {
                // 先隐藏其他 Fragment
                supportFragmentManager.beginTransaction().hide(frag).commit()
            }
        }
        supportFragmentManager.beginTransaction().show(fragment).commit()
    }

    /**
     * 初始化侧滑菜单和导航菜单
     */
    private fun initDrawer() {
        val toggle = ActionBarDrawerToggle(
            this, binding.mainDrawerLayout, binding.mainPage.mainToolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.mainDrawerLayout.addDrawerListener(toggle)

        // 尝试先从 FragmentManager 中获取 Fragment 对象
        // 在屏幕旋转等场景下，Activity 销毁重建后，会保留 Fragment 实例
        drawerFragment = supportFragmentManager.findFragmentByTag(DRAWER_FRAGMENT_TAG)
        if (drawerFragment == null) {
            drawerFragment = DrawerFragment()
        }
        drawerFragment?.let {
            val transition = supportFragmentManager.beginTransaction()
            if (it.isAdded) {
                transition.show(it)
            } else {
                transition.add(R.id.drawer_container, it, DRAWER_FRAGMENT_TAG)
            }
            transition.commitAllowingStateLoss()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_search ->                 //打开搜索界面
                startActivity(Intent(this, SearchActivity::class.java))
        }
        return true
    }

    override fun openNavWebDetail(context: Context, title: String, url: String) {
        val param = DetailParam(0, title, url, DetailParam.DetailType.WEBPAGE)
        ArticleDetailActivity.actionStart(context, param)
    }

    override fun openDiscoverWebDetail(context: Context, title: String, url: String) {
        val param = DetailParam(0, title, url, DetailParam.DetailType.WEBPAGE)
        ArticleDetailActivity.actionStart(context, param)
    }

    override fun onMineAction(fragment: Fragment, action: MineAction) {
        if (action == MineAction.ClickSystemSettings) {
            startActivity(Intent(this, KmpSettingsActivity::class.java))
            return
        }

        val msg = when (action) {
            MineAction.ClickProfile -> "点击：个人信息"
            MineAction.ClickMyPoints -> "点击：我的积分"
            MineAction.ClickPointsRank -> "点击：积分排行"
            MineAction.ClickMyShare -> "点击：我的分享"
            MineAction.ClickMyCollect -> "点击：我的收藏"
            MineAction.ClickBrowseHistory -> "点击：浏览历史"
            MineAction.ClickOpenSourceLicense -> "点击：开源许可"
            MineAction.ClickAboutAuthor -> "点击：关于作者"
            MineAction.ClickSystemSettings -> error("handled above")
        }
        ToastUtil.showToast(msg)
    }

    companion object {
        private const val DRAWER_FRAGMENT_TAG = "main_drawer_fragment_tag"
        private const val HOME_FRAGMENT_TAG = "main_home_fragment_tag"
        private const val KNOWLEDGE_FRAGMENT_TAG = "main_knowledge_fragment_tag"
        private const val DISCOVER_FRAGMENT_TAG = "main_discover_fragment_tag"
        private const val NAV_FRAGMENT_TAG = "main_nav_fragment_tag"
        private const val MINE_FRAGMENT_TAG = "main_mine_fragment_tag"
    }
}
