package cc.lixiaoyu.wanandroid.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cc.lixiaoyu.wanandroid.core.projectdata.ProjectDataFragment;
import cc.lixiaoyu.wanandroid.core.wechat.WechatDataFragment;
import cc.lixiaoyu.wanandroid.entity.ProjectTitle;
import cc.lixiaoyu.wanandroid.entity.WechatTitle;

public class WechatAdapter extends FragmentPagerAdapter {
    private List<WechatTitle> mDataList;
    private List<Fragment> mFragmentList;
    public WechatAdapter(FragmentManager fm, List<WechatTitle> dataList) {
        super(fm);
        this.mDataList = dataList;
        this.mFragmentList = new ArrayList<>(mDataList.size());
    }

    @Override
    public Fragment getItem(int i) {
       WechatDataFragment fragment = WechatDataFragment.newInstance(mDataList.get(i));
       mFragmentList.add(fragment);
       return fragment;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mDataList.get(position).getName();
    }

    public void jumpToListTop(int index) {
        ((WechatDataFragment)mFragmentList.get(index)).jumpToListTop();
    }
}
