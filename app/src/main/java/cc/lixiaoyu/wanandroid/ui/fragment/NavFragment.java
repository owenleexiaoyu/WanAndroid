package cc.lixiaoyu.wanandroid.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.adapter.NavAdapter;
import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.Nav;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.ui.activity.ArticleDetailActivity;
import cc.lixiaoyu.wanandroid.util.RetrofitUtil;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavFragment extends Fragment {
    @BindView(R.id.fnav_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.fnav_flowlayout)
    TagFlowLayout mFlowLayout;

    private Unbinder unbinder;
    private NavAdapter mAdapter;
    private List<Nav.NavItem> mNavItemList;
    public static NavFragment newInstance(){
        return new NavFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav, container, false);

        unbinder = ButterKnife.bind(this, view);
        mNavItemList = new ArrayList<>();
        mAdapter = new NavAdapter(R.layout.item_nav, new ArrayList<Nav>(0));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.setCurrentItem(position);
                mAdapter.notifyDataSetChanged();
                mNavItemList.clear();
                mNavItemList.addAll(mAdapter.getData().get(position).getItems());
                mFlowLayout.onChanged();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        WanAndroidService service = RetrofitUtil.getWanAndroidService();
        Call<WanAndroidResult<List<Nav>>> call = service.getNavData();
        call.enqueue(new Callback<WanAndroidResult<List<Nav>>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<List<Nav>>> call,
                                   Response<WanAndroidResult<List<Nav>>> response) {
                WanAndroidResult<List<Nav>> result = response.body();
                if(result.getErrorCode() == 0){
                    //请求成功
                    mAdapter.addData(result.getData());
                    mNavItemList = result.getData().get(0).getItems();
                    mFlowLayout.setAdapter(new TagAdapter<Nav.NavItem>(mNavItemList){

                        @Override
                        public View getView(FlowLayout parent, int position, Nav.NavItem navItem) {
                            TextView tv = (TextView) LayoutInflater
                                    .from(getActivity())
                                    .inflate(R.layout.layout_nav_item, mFlowLayout, false);
                            tv.setText(navItem.getTitle());
                            return tv;
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<WanAndroidResult<List<Nav>>> call, Throwable t) {
                ToastUtil.showToast("请求出错");
                t.printStackTrace();
            }
        });
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Nav.NavItem item = mNavItemList.get(position);
                ArticleDetailActivity.actionStart(getActivity(), item.getTitle(), item.getLink());
                return true;
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
