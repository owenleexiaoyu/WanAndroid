package cc.lixiaoyu.wanandroid.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cc.lixiaoyu.wanandroid.entity.ProjectTitle;
import cc.lixiaoyu.wanandroid.core.projectdata.ProjectDataModel;
import cc.lixiaoyu.wanandroid.core.projectdata.ProjectDataPresenter;
import cc.lixiaoyu.wanandroid.core.projectdata.ProjectDataFragment;

public class ProjectAdapter extends FragmentPagerAdapter {
    private List<ProjectTitle> mDataList;
    public ProjectAdapter(FragmentManager fm, List<ProjectTitle> dataList) {
        super(fm);
        this.mDataList = dataList;
    }

    @Override
    public Fragment getItem(int i) {
       ProjectDataFragment fragment = ProjectDataFragment.newInstance(mDataList.get(i));
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
}
