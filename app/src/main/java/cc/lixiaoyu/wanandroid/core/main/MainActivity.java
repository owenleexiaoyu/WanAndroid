package cc.lixiaoyu.wanandroid.core.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import cc.lixiaoyu.wanandroid.ui.activity.AboutActivity;
import cc.lixiaoyu.wanandroid.ui.activity.LoginActivity;
import cc.lixiaoyu.wanandroid.ui.activity.TodoActivity;
import cc.lixiaoyu.wanandroid.ui.fragment.NavFragment;
import cc.lixiaoyu.wanandroid.ui.fragment.ProjectFragment;
import cc.lixiaoyu.wanandroid.ui.fragment.WechatFragment;


public class MainActivity extends MVPBaseActivity<MainPresenter> implements MainContract.View{

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

    private Fragment mCurrentFragment = new Fragment();//当前的fragment，用于切换
    private List<Fragment>  mFragmentList;
    private FragmentManager mManager;
    //当前Activity所装载的Fragment的序号
    private int mCurrentFragmentIndex = 0;

    public String [] titles = {"首页","体系","公众号","导航","项目"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
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
        mManager = getSupportFragmentManager();
        mFragmentList = new ArrayList<>();
        HomeFragment homeFragment = HomeFragment.newInstance();
        KnowledgeTreeFragment knowledgeFragment = KnowledgeTreeFragment.newInstanse();
        WechatFragment wechatFragment = WechatFragment.newInstance();
        NavFragment navFragment = NavFragment.newInstance();
        ProjectFragment projectFragment = ProjectFragment.newInstance();

        mFragmentList.add(homeFragment);
        mFragmentList.add(knowledgeFragment);
        mFragmentList.add(wechatFragment);
        mFragmentList.add(navFragment);
        mFragmentList.add(projectFragment);
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        initToolbar();
        initBottomNavBar();
        //展示第一个Fragment
        loadFragment(mFragmentList.get(0));
        initDrawerAndNavigationView();
        //控制置顶按钮的显示与隐藏
        showOrHideUpButton();
        //处理向上置顶按钮的事件
        mBtnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mCurrentFragmentIndex){
                    case 0:
                        ((HomeFragment)mFragmentList.get(mCurrentFragmentIndex)).jumpToListTop();
                        break;
                    case 1:
                        ((KnowledgeTreeFragment)mFragmentList.get(mCurrentFragmentIndex)).jumpToListTop();
                        break;
                    case 2:
                        ((WechatFragment)mFragmentList.get(mCurrentFragmentIndex)).jumpToListTop();
                        break;
                    case 4:
                        ((ProjectFragment)mFragmentList.get(mCurrentFragmentIndex)).jumpToListTop();
                        break;
                }
            }
        });
    }

    /**
     * 控制置顶按钮的显示与隐藏
     */
    private void showOrHideUpButton() {
        if(mCurrentFragmentIndex == 3){
            mBtnUp.setVisibility(View.GONE);
        }else{
            mBtnUp.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar(){
        mToolbar.setTitle(titles[0]);
        setSupportActionBar(mToolbar);
    }

    /**
     * 初始化底部导航栏
     */
    private void initBottomNavBar(){
        mBottomNavBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavBar.addItem(new BottomNavigationItem(R.mipmap.ic_home, "首页").setActiveColor(R.color.orange))
                .addItem(new BottomNavigationItem(R.mipmap.ic_system, "体系").setActiveColor(R.color.orange))
                .addItem(new BottomNavigationItem(R.mipmap.ic_wechat, "公众号").setActiveColor(R.color.orange))
                .addItem(new BottomNavigationItem(R.mipmap.ic_nav, "导航").setActiveColor(R.color.orange))
                .addItem(new BottomNavigationItem(R.mipmap.ic_project_gray, "项目").setActiveColor(R.color.orange))
                .setFirstSelectedPosition(0)
                .initialise();
        mBottomNavBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mToolbar.setTitle(titles[position]);
                mCurrentFragmentIndex = position;
                showOrHideUpButton();
                loadFragment(mFragmentList.get(position));
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
     * @param fragment
     */
    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = mManager.beginTransaction();
        //首先判断要加载的Fragment是否已经被添加过，如果没有则添加，如果有则直接show
        if(!fragment.isAdded()){
            if(mCurrentFragment != null){
                transaction.hide(mCurrentFragment);
            }
            transaction.add(R.id.main_container, fragment);
        }
        else{
            transaction.hide(mCurrentFragment).show(fragment);
        }
        mCurrentFragment = fragment;
        transaction.commit();
    }

    /**
     * 初始化侧滑菜单和导航菜单
     */
    private void initDrawerAndNavigationView() {
        //判断是否登录
        if(mPresenter.getLoginState()){
            showLoginView();
        }else{
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
                switch (menuItem.getItemId()){
                    case R.id.nav_collection:
                        //判断是否登录过，没有登录要先登录
                        if(mPresenter.getLoginState()){
                            //登录后进入收藏界面
                            startActivity(new Intent(MainActivity.this, CollectionActivity.class));
                        }else{
                            //未登录则进入登录界面
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }
                        break;
                    case R.id.nav_todo:
                        //判断是否登录过，没有登录要先登录
                        if(mPresenter.getLoginState()){
                            //登录后进入TODO界面
                            startActivity(new Intent(MainActivity.this, TodoActivity.class));
                        }else{
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
        switch (item.getItemId()){
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
        if(mNavigationView == null){
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
        if(mNavigationView == null){
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
}
