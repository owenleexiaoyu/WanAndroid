package cc.lixiaoyu.wanandroid.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
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
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.base.BaseActivity;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.mvp.model.HomeModel;
import cc.lixiaoyu.wanandroid.mvp.model.KnowledgeTreeModel;
import cc.lixiaoyu.wanandroid.mvp.presenter.HomePresenter;
import cc.lixiaoyu.wanandroid.mvp.presenter.KnowledgeTreePresenter;
import cc.lixiaoyu.wanandroid.mvp.view.HomeFragment;
import cc.lixiaoyu.wanandroid.mvp.view.KnowledgeTreeFragment;
import cc.lixiaoyu.wanandroid.ui.fragment.NavFragment;
import cc.lixiaoyu.wanandroid.ui.fragment.ProjectFragment;
import cc.lixiaoyu.wanandroid.util.DataManager;
import cc.lixiaoyu.wanandroid.util.RetrofitUtil;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.main_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.main_nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_bottom_nav_bar)
    BottomNavigationBar mBottomNavBar;

    private List<Fragment>  mFragmentList;
    public String [] titles = {"首页","体系","导航","项目"};

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
        NavFragment navFragment = NavFragment.newInstance();
        ProjectFragment projectFragment = ProjectFragment.newInstance();

        mFragmentList.add(homeFragment);
        mFragmentList.add(knowledgeFragment);
        mFragmentList.add(navFragment);
        mFragmentList.add(projectFragment);

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
        mNavigationView.setCheckedItem(R.id.nav_collection);
        mNavigationView.setNavigationItemSelectedListener(this);

        //展示第一个Fragment
        final FragmentManager manager = getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.main_container, mFragmentList.get(0));
        transaction.commit();

        mBottomNavBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavBar.addItem(new BottomNavigationItem(R.mipmap.ic_home_gray, "首页").setActiveColor(R.color.orange))
                .addItem(new BottomNavigationItem(R.mipmap.ic_system_gray, "体系").setActiveColor(R.color.orange))
                .addItem(new BottomNavigationItem(R.mipmap.ic_nav_gray, "导航").setActiveColor(R.color.orange))
                .addItem(new BottomNavigationItem(R.mipmap.ic_project_gray, "项目").setActiveColor(R.color.orange))
                .setFirstSelectedPosition(0)
                .initialise();
        mBottomNavBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mToolbar.setTitle(titles[position]);
                FragmentTransaction transaction1 = manager.beginTransaction();
                transaction1.replace(R.id.main_container, mFragmentList.get(position));
                transaction1.commit();
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });


        mToolbar.setTitle(titles[0]);
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //未登录时显示登录，登录后显示用户名
        TextView tvUserNameOrLogin = mNavigationView.getHeaderView(0).findViewById(R.id.main_tv_username);
        //未登录时退出登陆的menuItem隐藏，登录后显示
        Menu menu = mNavigationView.getMenu();
        MenuItem logoutItem = menu.findItem(R.id.nav_logout);
        if(isLogined()){
            tvUserNameOrLogin.setText(DataManager.getCurrentUser().getUsername());
            logoutItem.setVisible(true);
        }else{
            tvUserNameOrLogin.setText("登录");
            tvUserNameOrLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });
            logoutItem.setVisible(false);
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_logout:
                logout();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        WanAndroidService service = RetrofitUtil.getWanAndroidService();
        Call<WanAndroidResult<String>> call = service.logout();
        call.enqueue(new Callback<WanAndroidResult<String>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<String>> call,
                                   Response<WanAndroidResult<String>> response) {
                WanAndroidResult<String> result = response.body();
                if(result.getErrorCode() == 0){
                    DataManager.clearCurrentUser();
                    ToastUtil.showToast("退出登录成功");
                }
            }

            @Override
            public void onFailure(Call<WanAndroidResult<String>> call, Throwable t) {
                ToastUtil.showToast("退出登录失败，出错了");
            }
        });
    }
}
