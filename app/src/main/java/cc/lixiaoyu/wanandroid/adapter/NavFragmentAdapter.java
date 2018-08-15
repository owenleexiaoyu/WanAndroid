package cc.lixiaoyu.wanandroid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cc.lixiaoyu.wanandroid.R;

public class NavFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    private Context mContext;
    public String [] titles = {"首页","体系","导航","项目"};
    public int[] iconNormalIds = {R.mipmap.ic_home_gray,R.mipmap.ic_system_gray,
            R.mipmap.ic_nav_gray, R.mipmap.ic_project_gray};
    public int[] iconSelectedIds = {R.mipmap.ic_home_orange,R.mipmap.ic_system_orange,
            R.mipmap.ic_nav_orange, R.mipmap.ic_project_orange};
    public NavFragmentAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList) {
        super(fm);
        mContext = context;
        mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public View getTabView(int position){
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_tab_item,null);
        ImageView imgIcon = view.findViewById(R.id.tab_item_img);
        TextView tvTitle = view.findViewById(R.id.tab_item_text);
        tvTitle.setText(titles[position]);
        if(position == 0){
            imgIcon.setImageResource(iconSelectedIds[position]);
            tvTitle.setTextColor(mContext.getColor(R.color.orange));
        }else{
            imgIcon.setImageResource(iconNormalIds[position]);
        }
        return view;
    }

    public int[] getIconNormalIds() {
        return iconNormalIds;
    }

    public int[] getIconSelectedIds() {
        return iconSelectedIds;
    }

    public String[] getTitles() {
        return titles;
    }
}
