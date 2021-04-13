package cc.lixiaoyu.wanandroid.core.project;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cc.lixiaoyu.wanandroid.entity.ProjectTitle;
import cc.lixiaoyu.wanandroid.core.projectdata.ProjectDataFragment;

public class ProjectAdapter extends FragmentPagerAdapter {
    private List<ProjectTitle> mDataList;
    private List<Fragment> mFragmentList;
    public ProjectAdapter(FragmentManager fm, List<ProjectTitle> dataList) {
        super(fm);
        this.mDataList = dataList;
        this.mFragmentList = new ArrayList<>(mDataList.size());
    }

    @Override
    public Fragment getItem(int i) {
       ProjectDataFragment fragment = ProjectDataFragment.newInstance(mDataList.get(i));
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
        ((ProjectDataFragment)mFragmentList.get(index)).jumpToListTop();
    }
}
