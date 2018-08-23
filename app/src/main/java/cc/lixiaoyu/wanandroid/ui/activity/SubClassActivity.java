package cc.lixiaoyu.wanandroid.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.adapter.SubClassAdapter;
import cc.lixiaoyu.wanandroid.base.BaseSwipeBackActivity;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.mvp.presenter.SubClassPresenter;

public class SubClassActivity extends BaseSwipeBackActivity {

    @BindView(R.id.subclass_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.subclass_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.subclass_viewpager)
    ViewPager mViewPager;

    private SubClassAdapter mAdapter;
    private PrimaryClass mPrimaryClass;
    private List<PrimaryClass.SubClass> mDataList;

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mPrimaryClass = (PrimaryClass) intent.getSerializableExtra("primaryclass");
        mDataList = mPrimaryClass.getChildren();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mToolbar.setTitle(mPrimaryClass.getName());
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        mAdapter = new SubClassAdapter(getSupportFragmentManager(), mDataList);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected int attachLayout() {
        return R.layout.activity_subclass;
    }

    /**
     * 携带具体数据从其他Activity跳转到这个Activity，代替startActivity
     * @param context
     * @param primaryClass
     */
    public static void actionStart(Context context, PrimaryClass primaryClass){
        Intent intent = new Intent(context, SubClassActivity.class);
        intent.putExtra("primaryclass", primaryClass);
        context.startActivity(intent);
    }
}
