package cc.lixiaoyu.wanandroid.core.subclass;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.base.BaseSwipeBackActivity;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;

public class SubClassActivity extends BaseSwipeBackActivity {

    @BindView(R.id.subclass_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.subclass_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.subclass_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.subclass_btn_up)
    FloatingActionButton mBtnUp;

    private SubClassAdapter mAdapter;
    private PrimaryClass mPrimaryClass;
    private List<PrimaryClass.SubClass> mDataList;
    //当前加载的Fragment的序号
    private int mCurrentFragmentIndex = 0;
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
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mCurrentFragmentIndex = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
        mBtnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.jumpToListTop(mCurrentFragmentIndex);
            }
        });
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
