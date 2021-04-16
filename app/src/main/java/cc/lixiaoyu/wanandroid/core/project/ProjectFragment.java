package cc.lixiaoyu.wanandroid.core.project;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.base.BaseFragment;
import cc.lixiaoyu.wanandroid.entity.ProjectTitle;
import cc.lixiaoyu.wanandroid.util.RetrofitHelper;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProjectFragment extends BaseFragment {

    @BindView(R.id.fproject_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.fproject_viewpager)
    ViewPager mViewPager;
    private ProjectAdapter mAdapter;

    private List<ProjectTitle> mDataList;

    //当前加载的子fragment的序号
    private int mCurrentChildFragmentIndex = 0;

    public static ProjectFragment newInstance() {
        return new ProjectFragment();
    }

    @Override
    protected void initData() {

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView(View view) {
        mDataList = new ArrayList<>();

        mAdapter = new ProjectAdapter(getChildFragmentManager(), mDataList);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mCurrentChildFragmentIndex = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        RetrofitHelper.getInstance().getWanAndroidService().getProjectsData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    mDataList.addAll(result.getData());
                    mAdapter.notifyDataSetChanged();
                }, throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.showToast("出错了");
                });
    }

    @Override
    protected int attachLayout() {
        return R.layout.fragment_project;
    }

    /**
     * 回到列表顶部
     */
    public void jumpToListTop() {
        //将置顶功能进一步交给Adapter实现
        mAdapter.jumpToListTop(mCurrentChildFragmentIndex);
    }
}
