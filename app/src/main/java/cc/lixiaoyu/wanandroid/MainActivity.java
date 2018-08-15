package cc.lixiaoyu.wanandroid;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cc.lixiaoyu.wanandroid.adapter.NavFragmentAdapter;
import cc.lixiaoyu.wanandroid.fragment.HomeFragment;
import cc.lixiaoyu.wanandroid.fragment.NavFragment;
import cc.lixiaoyu.wanandroid.fragment.ProjectFragment;
import cc.lixiaoyu.wanandroid.fragment.SystemFragment;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private NavFragmentAdapter mAdapter;
    private List<Fragment>  mFragmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();


    }

    private void initView() {
        mTabLayout = findViewById(R.id.main_navbar);
        mViewPager = findViewById(R.id.main_viewpager);

        mAdapter = new NavFragmentAdapter(getSupportFragmentManager(), this, mFragmentList);
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
    }

    private void changeTab(TabLayout.Tab tab) {
        for(int i = 0;i<mAdapter.getCount();i++){
            View view = mTabLayout.getTabAt(i).getCustomView();
            ImageView imgIcon = view.findViewById(R.id.tab_item_img);
            TextView tvTitle = view.findViewById(R.id.tab_item_text);
            if(i == tab.getPosition()){
                imgIcon.setImageResource(mAdapter.getIconSelectedIds()[i]);
                tvTitle.setTextColor(getColor(R.color.orange));
            }else{
                imgIcon.setImageResource(mAdapter.getIconNormalIds()[i]);
                tvTitle.setTextColor(getColor(R.color.gray));
            }
        }
    }

    private void initData() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new SystemFragment());
        mFragmentList.add(new NavFragment());
        mFragmentList.add(new ProjectFragment());
    }
}
