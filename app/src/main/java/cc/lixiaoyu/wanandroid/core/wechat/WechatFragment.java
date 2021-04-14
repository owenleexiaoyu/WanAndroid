package cc.lixiaoyu.wanandroid.core.wechat;

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
import cc.lixiaoyu.wanandroid.entity.WanAndroidResponse;
import cc.lixiaoyu.wanandroid.entity.WechatTitle;
import cc.lixiaoyu.wanandroid.util.RetrofitHelper;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WechatFragment extends Fragment {
    private Unbinder unbinder;

    @BindView(R.id.fwechat_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.fwechat_viewpager)
    ViewPager mViewPager;
    private WechatAdapter mAdapter;

    private WanAndroidService mService;
    private List<WechatTitle> mDataList;
    //当前加载的子fragment的序号
    private int mCurrentChildFragmentIndex = 0;
    public static WechatFragment newInstance(){
        return new WechatFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat,container, false);
        //绑定ButterKnife
        unbinder = ButterKnife.bind(this, view);
        mService = RetrofitHelper.getInstance().getWanAndroidService();
        mDataList = new ArrayList<>();

        mAdapter = new WechatAdapter(getChildFragmentManager(), mDataList);
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

        mService.getWetchatPublicTitles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WanAndroidResponse<List<WechatTitle>>>() {
            @Override
            public void accept(WanAndroidResponse<List<WechatTitle>> result) throws Exception {
                mDataList.addAll(result.getData());
                mAdapter.notifyDataSetChanged();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
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
