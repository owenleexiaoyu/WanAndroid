package cc.lixiaoyu.wanandroid.core.project;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.base.BaseFragment;
import cc.lixiaoyu.wanandroid.entity.ProjectTitle;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResponse;
import cc.lixiaoyu.wanandroid.util.RetrofitHelper;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectFragment extends BaseFragment {

    @BindView(R.id.fproject_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.fproject_viewpager)
    ViewPager mViewPager;
    private ProjectAdapter mAdapter;

    private WanAndroidService mService;
    private List<ProjectTitle> mDataList;

    //当前加载的子fragment的序号
    private int mCurrentChildFragmentIndex = 0;

    public static ProjectFragment newInstance(){
        return new ProjectFragment();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        mService = RetrofitHelper.getInstance().getWanAndroidService();
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
        Call<WanAndroidResponse<List<ProjectTitle>>> call = mService.getProjectsData();
        call.enqueue(new Callback<WanAndroidResponse<List<ProjectTitle>>>() {
            @Override
            public void onResponse(Call<WanAndroidResponse<List<ProjectTitle>>> call,
                                   Response<WanAndroidResponse<List<ProjectTitle>>> response) {
                WanAndroidResponse<List<ProjectTitle>> result = response.body();
                mDataList.addAll(result.getData());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<WanAndroidResponse<List<ProjectTitle>>> call, Throwable t) {
                ToastUtil.showToast("出错了");
            }
        });
    }

    @Override
    protected int attachLayout() {
        return R.layout.fragment_project;
    }

    /**
     * 回到列表顶部
     */
    public void jumpToListTop(){
        //将置顶功能进一步交给Adapter实现
        mAdapter.jumpToListTop(mCurrentChildFragmentIndex);
    }
}
