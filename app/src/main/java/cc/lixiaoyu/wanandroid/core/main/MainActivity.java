package cc.lixiaoyu.wanandroid.core.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.base.MVPBaseActivity;
import cc.lixiaoyu.wanandroid.core.collections.CollectionActivity;
import cc.lixiaoyu.wanandroid.core.search.SearchActivity;
import cc.lixiaoyu.wanandroid.core.home.HomeFragment;
import cc.lixiaoyu.wanandroid.core.tree.KnowledgeTreeFragment;
import cc.lixiaoyu.wanandroid.core.about.AboutActivity;
import cc.lixiaoyu.wanandroid.core.login.LoginActivity;
import cc.lixiaoyu.wanandroid.core.todo.TodoActivity;
import cc.lixiaoyu.wanandroid.core.nav.NavFragment;
import cc.lixiaoyu.wanandroid.core.project.ProjectFragment;
import cc.lixiaoyu.wanandroid.core.wechat.WechatFragment;


public class MainActivity extends MVPBaseActivity<MainPresenter> implements MainContract.View {

    private static final String CURRENT_INDEX = "current_index";

    @BindView(R.id.main_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.main_nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_bottom_nav_bar)
    BottomNavigationBar mBottomNavBar;
    @BindView(R.id.main_btn_up)
    FloatingActionButton mBtnUp;
    private TextView mTvUserNameOrLogin;

    private Fragment mHomeFragment;
    private Fragment mKnowledgeFragment;
    private Fragment mWechatFragment;
    private Fragment mNavFragment;
    private Fragment mProjectFragment;

    private List<Fragment> mFragmentList = new ArrayList<>();
    //当前所在的 tab 序号
    private int mCurrentIndex = 0;

    public String[] titles = {"首页", "体系", "公众号", "导航", "项目"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // 这一步要放在调用 super.onCreate 之前
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(CURRENT_INDEX);
        }
        super.onCreate(savedInstanceState);
        mPresenter.start();
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    /**
     * 初始化数据源
     */
    @Override
    protected void initData() {
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        initToolbar();
        initBottomNavBar();
        //展示第一个Fragment
        loadFragment(mCurrentIndex);
        initDrawerAndNavigationView();
        //控制置顶按钮的显示与隐藏
        showOrHideUpButton();
        //处理向上置顶按钮的事件
        mBtnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mCurrentIndex) {
                    case 0:
                        ((HomeFragment) mFragmentList.get(mCurrentIndex)).jumpToListTop();
                        break;
                    case 1:
                        ((KnowledgeTreeFragment) mFragmentList.get(mCurrentIndex)).jumpToListTop();
                        break;
                    case 2:
                        ((WechatFragment) mFragmentList.get(mCurrentIndex)).jumpToListTop();
                        break;
                    case 4:
                        ((ProjectFragment) mFragmentList.get(mCurrentIndex)).jumpToListTop();
                        break;
                }
            }
        });
    }

    /**
     * 控制置顶按钮的显示与隐藏
     */
    private void showOrHideUpButton() {
        if (mCurrentIndex == 3) {
            mBtnUp.setVisibility(View.GONE);
        } else {
            mBtnUp.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        mToolbar.setTitle(titles[0]);
        setSupportActionBar(mToolbar);
    }

    /**
     * 初始化底部导航栏
     */
    private void initBottomNavBar() {
        mBottomNavBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavBar.addItem(new BottomNavigationItem(R.drawable.ic_home, "首页").setActiveColor(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.ic_system, "体系").setActiveColor(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.ic_wechat, "公众号").setActiveColor(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.ic_nav, "导航").setActiveColor(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.ic_project_gray, "项目").setActiveColor(R.color.colorPrimary))
                .setFirstSelectedPosition(mCurrentIndex)
                .initialise();
        mBottomNavBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mToolbar.setTitle(titles[position]);
                mCurrentIndex = position;
                showOrHideUpButton();
                loadFragment(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    /**
     * 加载Fragment
     *
     * @param index
     */
    private void loadFragment(int index) {
        switch (index) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.newInstance();
                }
                addAndShowFragment(mHomeFragment);
                break;
            case 1:
                if (mKnowledgeFragment == null) {
                    mKnowledgeFragment = KnowledgeTreeFragment.newInstanse();
                }
                addAndShowFragment(mKnowledgeFragment);
                break;
            case 2:
                if (mWechatFragment == null) {
                    mWechatFragment = WechatFragment.newInstance();
                }
                addAndShowFragment(mWechatFragment);
                break;
            case 3:
                if (mNavFragment == null) {
                    mNavFragment = NavFragment.newInstance();
                }
                addAndShowFragment(mNavFragment);
                break;
            case 4:
                if (mProjectFragment == null) {
                    mProjectFragment = ProjectFragment.newInstance();
                }
                addAndShowFragment(mProjectFragment);
                break;
            default:
                throw new IllegalArgumentException("Index ["+ index+ "] is not support");
        }
    }


    private void addAndShowFragment(Fragment fragment) {
        if (fragment == null) return;
        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().add(R.id.main_container, fragment).commit();
            mFragmentList.add(fragment);
        }
        for (Fragment frag : mFragmentList) {
            if (frag != fragment) {
                // 先隐藏其他 Fragment
                getSupportFragmentManager().beginTransaction().hide(frag).commit();
            }
        }
        getSupportFragmentManager().beginTransaction().show(fragment).commit();
    }

    /**
     * 初始化侧滑菜单和导航菜单
     */
    private void initDrawerAndNavigationView() {
        //判断是否登录
        if (mPresenter.getLoginState()) {
            showLoginView();
        } else {
            showLogoutView();
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.
                OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_collection:
                        //判断是否登录过，没有登录要先登录
                        if (mPresenter.getLoginState()) {
                            //登录后进入收藏界面
                            startActivity(new Intent(MainActivity.this, CollectionActivity.class));
                        } else {
                            //未登录则进入登录界面
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }
                        break;
                    case R.id.nav_todo:
                        //判断是否登录过，没有登录要先登录
                        if (mPresenter.getLoginState()) {
                            //登录后进入TODO界面
                            startActivity(new Intent(MainActivity.this, TodoActivity.class));
                        } else {
                            //未登录则进入登录界面
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }
                        break;
                    case R.id.nav_about:
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        break;
                    case R.id.nav_logout:
                        logout();
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    protected int attachLayout() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_search:
                //打开搜索界面
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }
        return true;
    }

    /**
     * 退出登录
     */
    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setTitle("提示")
                .setMessage("确认退出登录吗")
                .setCancelable(true)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.logout();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoginView() {
        if (mNavigationView == null) {
            return;
        }
        //登录后显示用户名
        mTvUserNameOrLogin = mNavigationView.getHeaderView(0).findViewById(R.id.main_tv_username);
        mTvUserNameOrLogin.setText(mPresenter.getLoginAccount());
        mTvUserNameOrLogin.setOnClickListener(null);
        //登录后显示退出登陆的menuItem
        mNavigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
    }

    @Override
    public void showLogoutView() {
        if (mNavigationView == null) {
            return;
        }
        //未登录时显示登录
        mTvUserNameOrLogin = mNavigationView.getHeaderView(0).findViewById(R.id.main_tv_username);
        mTvUserNameOrLogin.setText("登录");
        mTvUserNameOrLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        //未登录时退出登陆的menuItem隐藏
        mNavigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
    }

    @Override
    public void showAutoLoginView() {
        showLoginView();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CURRENT_INDEX, mCurrentIndex);
    }

}
