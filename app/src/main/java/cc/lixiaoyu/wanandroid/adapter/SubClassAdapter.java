package cc.lixiaoyu.wanandroid.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.ui.fragment.SubClassFragment;

public class SubClassAdapter extends FragmentPagerAdapter {

    private List<PrimaryClass.SubClass> subClassList;

    public SubClassAdapter(FragmentManager fm, List<PrimaryClass.SubClass> subClassList) {
        super(fm);
        this.subClassList = subClassList;
    }

    @Override
    public Fragment getItem(int i) {
        return SubClassFragment.newInstance(subClassList.get(i));
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
