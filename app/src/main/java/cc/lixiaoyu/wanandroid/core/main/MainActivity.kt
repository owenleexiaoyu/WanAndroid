package cc.lixiaoyu.wanandroid.core.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import cc.lixiaoyu.wanandroid.core.nav.NavFragment
import cc.lixiaoyu.wanandroid.core.project.ProjectFragment
import cc.lixiaoyu.wanandroid.core.search.SearchActivity
import cc.lixiaoyu.wanandroid.core.tree.KnowledgeSystemFragment
import cc.lixiaoyu.wanandroid.core.wechat.WechatFragment
import cc.lixiaoyu.wanandroid.databinding.ActivityMainBinding
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainVM: MainViewModel

    private var homeFragment: Fragment? = null
    private var knowledgeFragment: Fragment? = null
    private var wechatFragment: Fragment? = null
    private var navFragment: Fragment? = null
    private var projectFragment: Fragment? = null
    private val fragmentMap: MutableMap<String, Fragment> = mutableMapOf()

    private var drawerFragment: Fragment? = null

    private val titleResIds = intArrayOf(
        R.string.home_page,
        R.string.knowledge_system,
        R.string.wechat_blog,
        R.string.navigation,
        R.string.project
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            homeFragment = supportFragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG)
            homeFragment?.let {
                fragmentMap.put(HOME_FRAGMENT_TAG, it)
            }
            knowledgeFragment = supportFragmentManager.findFragmentByTag(KNOWLEDGE_FRAGMENT_TAG)
            knowledgeFragment?.let {
                fragmentMap.put(KNOWLEDGE_FRAGMENT_TAG, it)
            }
            wechatFragment = supportFragmentManager.findFragmentByTag(WECHAT_FRAGMENT_TAG)
            wechatFragment?.let {
                fragmentMap.put(WECHAT_FRAGMENT_TAG, it)
            }
            navFragment = supportFragmentManager.findFragmentByTag(NAV_FRAGMENT_TAG)
            navFragment?.let {
                fragmentMap.put(NAV_FRAGMENT_TAG, it)
            }
            projectFragment = supportFragmentManager.findFragmentByTag(PROJECT_FRAGMENT_TAG)
            projectFragment?.let {
                fragmentMap.put(PROJECT_FRAGMENT_TAG, it)
            }
            drawerFragment = supportFragmentManager.findFragmentByTag(DRAWER_FRAGMENT_TAG)
        }
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
            binding.mainPage.mainBtnUp.visibility = if (index == 3) View.GONE else View.VISIBLE
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
            when (val index = mainVM.currentTabIndex.value ?: 0) {
                0 -> (fragmentMap[HOME_FRAGMENT_TAG] as HomeFragment).jumpToListTop()
                1 -> (fragmentMap[KNOWLEDGE_FRAGMENT_TAG] as KnowledgeSystemFragment).jumpToListTop()
                2 -> (fragmentMap[WECHAT_FRAGMENT_TAG] as WechatFragment).jumpToListTop()
                4 -> (fragmentMap[PROJECT_FRAGMENT_TAG] as ProjectFragment).jumpToListTop()
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
                BottomNavigationItem(R.drawable.ic_wechat, getString(titleResIds[2]))
                    .setActiveColor(ContextCompat.getColor(context, R.color.ConstBrand))
            )
            addItem(
                BottomNavigationItem(R.drawable.ic_navigation, getString(titleResIds[3]))
                    .setActiveColor(ContextCompat.getColor(context, R.color.ConstBrand))
            )
            addItem(
                BottomNavigationItem(R.drawable.ic_project, getString(titleResIds[4]))
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
                if (homeFragment == null) {
                    homeFragment = HomeFragment.newInstance()
                }
                addAndShowFragment(requireNotNull(homeFragment), HOME_FRAGMENT_TAG)
            }
            1 -> {
                if (knowledgeFragment == null) {
                    knowledgeFragment = KnowledgeSystemFragment.newInstance()
                }
                addAndShowFragment(requireNotNull(knowledgeFragment), KNOWLEDGE_FRAGMENT_TAG)
            }
            2 -> {
                if (wechatFragment == null) {
                    wechatFragment = WechatFragment.newInstance()
                }
                addAndShowFragment(requireNotNull(wechatFragment), WECHAT_FRAGMENT_TAG)
            }
            3 -> {
                if (navFragment == null) {
                    navFragment = NavFragment.newInstance()
                }
                addAndShowFragment(requireNotNull(navFragment), NAV_FRAGMENT_TAG)
            }
            4 -> {
                if (projectFragment == null) {
                    projectFragment = ProjectFragment.newInstance()
                }
                addAndShowFragment(requireNotNull(projectFragment), PROJECT_FRAGMENT_TAG)
            }
            else -> throw IllegalArgumentException("Index [$index] is not support")
        }
    }

    private fun addAndShowFragment(fragment: Fragment, tag: String) {
        if (!fragment.isAdded) {
            supportFragmentManager.beginTransaction().add(R.id.main_container, fragment, tag).commit()
            fragmentMap[tag] = fragment
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
        toggle.syncState()

        if (drawerFragment == null) {
            drawerFragment = DrawerFragment()
        }
        if (supportFragmentManager.findFragmentByTag(DRAWER_FRAGMENT_TAG) != null) {
            return
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.drawer_container, requireNotNull(drawerFragment), DRAWER_FRAGMENT_TAG)
            .commitAllowingStateLoss()
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

    companion object {
        private const val DRAWER_FRAGMENT_TAG = "main_drawer_fragment_tag"
        private const val HOME_FRAGMENT_TAG = "main_home_fragment_tag"
        private const val KNOWLEDGE_FRAGMENT_TAG = "main_knowledge_fragment_tag"
        private const val WECHAT_FRAGMENT_TAG = "main_wechat_fragment_tag"
        private const val NAV_FRAGMENT_TAG = "main_nav_fragment_tag"
        private const val PROJECT_FRAGMENT_TAG = "main_project_fragment_tag"
    }
}