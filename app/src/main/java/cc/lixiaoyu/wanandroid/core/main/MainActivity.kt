package cc.lixiaoyu.wanandroid.core.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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

    private var mTvUserNameOrLogin: TextView? = null
    private var mHomeFragment: Fragment? = null
    private var mKnowledgeFragment: Fragment? = null
    private var mWechatFragment: Fragment? = null
    private var mNavFragment: Fragment? = null
    private var mProjectFragment: Fragment? = null
    private val mFragmentList: MutableList<Fragment> = ArrayList()

    private var drawerFragment: Fragment? = null

    //当前所在的 tab 序号
    private var mCurrentIndex = 0
    var titleResIds = intArrayOf(
        R.string.home_page,
        R.string.knowledge_system,
        R.string.wechat_blog,
        R.string.navigation,
        R.string.project
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        // 这一步要放在调用 super.onCreate 之前
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(CURRENT_INDEX)
        }
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initView()
        subscribeObserver()
    }

    private fun subscribeObserver() {

    }

    /**
     * 初始化控件
     */
    fun initView() {
        initToolbar()
        initBottomNavBar()
        //展示第一个Fragment
        loadFragment(mCurrentIndex)
        initDrawerAndNavigationView()
        //控制置顶按钮的显示与隐藏
        showOrHideUpButton()

        //处理向上置顶按钮的事件
        binding.mainPage.mainBtnUp.setOnClickListener {
            when (mCurrentIndex) {
                0 -> (mFragmentList[mCurrentIndex] as HomeFragment).jumpToListTop()
                1 -> (mFragmentList[mCurrentIndex] as KnowledgeSystemFragment).jumpToListTop()
                2 -> (mFragmentList[mCurrentIndex] as WechatFragment).jumpToListTop()
                4 -> (mFragmentList[mCurrentIndex] as ProjectFragment).jumpToListTop()
            }
        }
    }

    /**
     * 控制置顶按钮的显示与隐藏
     */
    private fun showOrHideUpButton() {
        if (mCurrentIndex == 3) {
            binding.mainPage.mainBtnUp.visibility = View.GONE
        } else {
            binding.mainPage.mainBtnUp.visibility = View.VISIBLE
        }
    }

    /**
     * 初始化Toolbar
     */
    private fun initToolbar() {
        binding.mainPage.mainToolbar.title = getString(titleResIds[0])
        setSupportActionBar(binding.mainPage.mainToolbar)
    }

    /**
     * 初始化底部导航栏
     */
    private fun initBottomNavBar() {
        binding.mainPage.mainBottomNavBar.setMode(BottomNavigationBar.MODE_FIXED)
        binding.mainPage.mainBottomNavBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
        binding.mainPage.mainBottomNavBar.addItem(
            BottomNavigationItem(R.drawable.ic_home, getString(titleResIds[0]))
                .setActiveColor(ContextCompat.getColor(this, R.color.ConstBrand))
            )
            .addItem(
                BottomNavigationItem(R.drawable.ic_knowledge_system, getString(titleResIds[1]))
                    .setActiveColor(ContextCompat.getColor(this, R.color.ConstBrand))
            )
            .addItem(
                BottomNavigationItem(R.drawable.ic_wechat, getString(titleResIds[2]))
                    .setActiveColor(ContextCompat.getColor(this, R.color.ConstBrand))
            )
            .addItem(
                BottomNavigationItem(R.drawable.ic_navigation, getString(titleResIds[3]))
                    .setActiveColor(ContextCompat.getColor(this, R.color.ConstBrand))
            )
            .addItem(
                BottomNavigationItem(R.drawable.ic_project, getString(titleResIds[4]))
                    .setActiveColor(ContextCompat.getColor(this, R.color.ConstBrand))
            )
            .setFirstSelectedPosition(mCurrentIndex)
            .initialise()
        binding.mainPage.mainBottomNavBar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                binding.mainPage.mainToolbar.title = getString(titleResIds[position])
                mCurrentIndex = position
                showOrHideUpButton()
                loadFragment(position)
            }

            override fun onTabUnselected(position: Int) {}
            override fun onTabReselected(position: Int) {}
        })
    }

    /**
     * 加载Fragment
     *
     * @param index
     */
    private fun loadFragment(index: Int) {
        when (index) {
            0 -> {
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.newInstance()
                }
                addAndShowFragment(mHomeFragment)
            }

            1 -> {
                if (mKnowledgeFragment == null) {
                    mKnowledgeFragment = KnowledgeSystemFragment.newInstance()
                }
                addAndShowFragment(mKnowledgeFragment)
            }

            2 -> {
                if (mWechatFragment == null) {
                    mWechatFragment = WechatFragment.newInstance()
                }
                addAndShowFragment(mWechatFragment)
            }

            3 -> {
                if (mNavFragment == null) {
                    mNavFragment = NavFragment.newInstance()
                }
                addAndShowFragment(mNavFragment)
            }

            4 -> {
                if (mProjectFragment == null) {
                    mProjectFragment = ProjectFragment.newInstance()
                }
                addAndShowFragment(mProjectFragment)
            }

            else -> throw IllegalArgumentException("Index [$index] is not support")
        }
    }

    private fun addAndShowFragment(fragment: Fragment?) {
        if (fragment == null) return
        if (!fragment.isAdded) {
            supportFragmentManager.beginTransaction().add(R.id.main_container, fragment).commit()
            mFragmentList.add(fragment)
        }
        for (frag in mFragmentList) {
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
    private fun initDrawerAndNavigationView() {
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

//    override fun showLoading() {}
//    override fun hideLoading() {}
//    override fun showLoginView() {
//        //登录后显示用户名
//        mTvUserNameOrLogin = binding.mainNavView.getHeaderView(0).findViewById<TextView>(R.id.main_tv_username)
//        mTvUserNameOrLogin?.let {
//            it.text = mPresenter!!.loginAccount
//            it.setOnClickListener(null)
//        }
//        //登录后显示退出登陆的menuItem
//        binding.mainNavView.menu.findItem(R.id.nav_logout).isVisible = true
//    }
//
//    override fun showLogoutView() {
//        //未登录时显示登录
//        mTvUserNameOrLogin = binding.mainNavView.getHeaderView(0).findViewById<TextView>(R.id.main_tv_username)
//        mTvUserNameOrLogin?.let {
//            it.text = getString(R.string.login)
//            it.setOnClickListener {
//                startActivity(
//                    Intent(
//                        this@MainActivity,
//                        LoginActivity::class.java
//                    )
//                )
//            }
//        }
//        //未登录时退出登陆的menuItem隐藏
//        binding.mainNavView.menu.findItem(R.id.nav_logout).isVisible = false
//    }
//
//    override fun showAutoLoginView() {
//        showLoginView()
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CURRENT_INDEX, mCurrentIndex)
    }

    companion object {
        private const val CURRENT_INDEX = "current_index"
        private const val DRAWER_FRAGMENT_TAG = "main_drawer_fragment_tag"
    }
}