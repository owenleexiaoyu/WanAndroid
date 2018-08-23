package cc.lixiaoyu.wanandroid.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.adapter.NavFragmentAdapter;
import cc.lixiaoyu.wanandroid.base.BaseActivity;
import cc.lixiaoyu.wanandroid.mvp.model.HomeModel;
import cc.lixiaoyu.wanandroid.mvp.model.KnowledgeTreeModel;
import cc.lixiaoyu.wanandroid.mvp.presenter.HomePresenter;
import cc.lixiaoyu.wanandroid.mvp.presenter.KnowledgeTreePresenter;
import cc.lixiaoyu.wanandroid.mvp.view.HomeFragment;
import cc.lixiaoyu.wanandroid.mvp.view.KnowledgeTreeFragment;
import cc.lixiaoyu.wanandroid.ui.fragment.NavFragment;
import cc.lixiaoyu.wanandroid.ui.fragment.ProjectFragment;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.main_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.main_nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_navbar)
    TabLayout mTabLayout;
    @BindView(R.id.main_viewpager)
    ViewPager mViewPager;

    private NavFragmentAdapter mAdapter;
    private List<Fragment>  mFragmentList;


    private HomePresenter mHomePresenter;
    private KnowledgeTreePresenter mKnowledgePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     * 初始化数据源
     */
    @Override
    protected void initData() {
        mFragmentList = new ArrayList<>();
        HomeFragment homeFragment = HomeFragment.newInstance();
        KnowledgeTreeFragment knowledgeFragment = KnowledgeTreeFragment.newInstanse();
        mFragmentList.add(homeFragment);
        mFragmentList.add(knowledgeFragment);
        mFragmentList.add(new NavFragment());
        mFragmentList.add(new ProjectFragment());

        mHomePresenter = new HomePresenter(homeFragment, new HomeModel());
        mKnowledgePresenter = new KnowledgeTreePresenter(knowledgeFragment, new KnowledgeTreeModel());

    }
    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        //ButterKnife注册
        ButterKnife.bind(this);
        mNavigationView.setCheckedItem(R.id.nav_camera);
        mNavigationView.setNavigationItemSelectedListener(this);
        mAdapter = new NavFragmentAdapter(getSupportFragmentManager(),
                this, mFragmentList);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTab(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        for(int i = 0;i<mAdapter.getCount();i++){
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(mAdapter.getTabView(i));
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                changeTab(tab);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mToolbar.setTitle(mAdapter.getPageTitle(0));
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected int attachLayout() {
        return R.layout.activity_main;
    }

    /**
     * 在底部导航栏tab切换时改变icon的颜色和text的颜色
     * @param tab
     */
    private void changeTab(TabLayout.Tab tab) {
        mToolbar.setTitle(mAdapter.getPageTitle(tab.getPosition()));
        for(int i = 0;i<mAdapter.getCount();i++){
            View view = mTabLayout.getTabAt(i).getCustomView();
            ImageView imgIcon = view.findViewById(R.id.tab_item_img);
            TextView tvTitle = view.findViewById(R.id.tab_item_text);
            if(i == tab.getPosition()){
                imgIcon.setImageResource(mAdapter.getIconSelectedIds()[i]);
                tvTitle.setTextColor(getColor(R.color.orange));
            }else{
                imgIcon.setImageResource(mAdapter.getIconNormalIds()[i]);
                tvTitle.setTextColor(getColor(R.color.light_gray));
            }
        }
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
                //
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
