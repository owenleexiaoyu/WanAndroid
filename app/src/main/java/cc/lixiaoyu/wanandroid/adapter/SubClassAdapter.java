package cc.lixiaoyu.wanandroid.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.core.subclass.SubClassModel;
import cc.lixiaoyu.wanandroid.core.subclass.SubClassPresenter;
import cc.lixiaoyu.wanandroid.core.subclass.SubClassFragment;

public class SubClassAdapter extends FragmentPagerAdapter {

    private List<PrimaryClass.SubClass> subClassList;

    public SubClassAdapter(FragmentManager fm, List<PrimaryClass.SubClass> subClassList) {
        super(fm);
        this.subClassList = subClassList;
    }

    @Override
    public Fragment getItem(int i) {
        SubClassFragment subClassFragment = SubClassFragment.newInstance(subClassList.get(i));
        return subClassFragment;
    }

    @Override
    public int getCount() {
        return subClassList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return subClassList.get(position).getName();
    }
}
