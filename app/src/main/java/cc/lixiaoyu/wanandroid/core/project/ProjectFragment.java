package cc.lixiaoyu.wanandroid.core.project;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.ProjectTitle;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.util.RetrofitHelper;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectFragment extends Fragment {
    private Unbinder unbinder;

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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project,container, false);
        //绑定ButterKnife
        unbinder = ButterKnife.bind(this, view);
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
        Call<WanAndroidResult<List<ProjectTitle>>> call = mService.getProjectsData();
        call.enqueue(new Callback<WanAndroidResult<List<ProjectTitle>>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<List<ProjectTitle>>> call,
                                   Response<WanAndroidResult<List<ProjectTitle>>> response) {
                WanAndroidResult<List<ProjectTitle>> result = response.body();
                mDataList.addAll(result.getData());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<WanAndroidResult<List<ProjectTitle>>> call, Throwable t) {
                ToastUtil.showToast("出错了");
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解绑ButterKnife
        unbinder.unbind();
    }

    /**
     * 回到列表顶部
     */
    public void jumpToListTop(){
        //将置顶功能进一步交给Adapter实现
        mAdapter.jumpToListTop(mCurrentChildFragmentIndex);
    }
}