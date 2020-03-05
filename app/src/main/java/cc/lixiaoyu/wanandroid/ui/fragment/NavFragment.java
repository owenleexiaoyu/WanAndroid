package cc.lixiaoyu.wanandroid.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import cc.lixiaoyu.wanandroid.util.RetrofitHelper;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

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
        WanAndroidService service = RetrofitHelper.getInstance().getWanAndroidService();
        service.getNavData()
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<WanAndroidResult<List<Nav>>>() {
                    @Override
                    public boolean test(WanAndroidResult<List<Nav>> result) throws Exception {
                        return result.getErrorCode() == 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WanAndroidResult<List<Nav>>>() {
                    @Override
                    public void accept(WanAndroidResult<List<Nav>> result) throws Exception {
                        mAdapter.addData(result.getData());
                        mNavItemList = result.getData().get(0).getItems();
                        mFlowLayout.setAdapter(new TagAdapter<Nav.NavItem>(mNavItemList) {

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
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable t) throws Exception {
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
