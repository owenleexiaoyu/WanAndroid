package cc.lixiaoyu.wanandroid.core.wechat;

import android.text.Html;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cc.lixiaoyu.wanandroid.entity.WechatTitle;

public class WechatAdapter extends FragmentPagerAdapter {
    private final List<WechatTitle> mDataList;
    private final List<Fragment> mFragmentList;
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
        return Html.fromHtml(mDataList.get(position).getName());
    }

    public void jumpToListTop(int index) {
        ((WechatDataFragment)mFragmentList.get(index)).jumpToListTop();
    }
}
