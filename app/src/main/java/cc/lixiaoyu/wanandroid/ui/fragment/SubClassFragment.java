package cc.lixiaoyu.wanandroid.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.adapter.ArticleAdapter;
import cc.lixiaoyu.wanandroid.base.BasePageFragment;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.ui.activity.ArticleDetailActivity;
import cc.lixiaoyu.wanandroid.util.AppConst;
import cc.lixiaoyu.wanandroid.util.RetrofitUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubClassFragment extends BasePageFragment {

    private PrimaryClass.SubClass mSubClass;
    private static final String ARGUMENTS_KEY = "subclass";
    private Unbinder unbinder;
    @BindView(R.id.fsubclass_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.fsubclass_swiperefreshlayout)
    SwipeRefreshLayout mRefreshLayout;
    private ArticleAdapter mAdapter;
    private List<ArticlePage.Article> mArticleList;

    public static SubClassFragment newInstance(PrimaryClass.SubClass subClass){
        Bundle bundle = new Bundle();
        SubClassFragment fragment = new SubClassFragment();
        bundle.putSerializable(ARGUMENTS_KEY,subClass);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!= null){
            mSubClass = (PrimaryClass.SubClass) bundle.getSerializable(ARGUMENTS_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subclass_article, container, false);

        unbinder = ButterKnife.bind(this, view);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareFetchData(true);
            }
        });
        mRefreshLayout.setRefreshing(true);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解除Butterknife绑定
        unbinder.unbind();
    }

    @Override
    public void fetchData() {
        mArticleList = new ArrayList<>();
        WanAndroidService service = RetrofitUtil.getWanAndroidService();
        Call<WanAndroidResult<ArticlePage>> call = service.getArticleList("0",mSubClass.getId()+"");
        call.enqueue(new Callback<WanAndroidResult<ArticlePage>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<ArticlePage>> call,
                                   Response<WanAndroidResult<ArticlePage>> response) {
                WanAndroidResult<ArticlePage> result = response.body();
                if(result.getErrorCode() == 0){
                    mArticleList.addAll(result.getData().getArticleList());
                    mAdapter.notifyDataSetChanged();
                    //停止刷新
                    mRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<WanAndroidResult<ArticlePage>> call, Throwable t) {

            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ArticleAdapter(getActivity(), mArticleList);
        mAdapter.setArticleItemClickListener(new ArticleAdapter.OnArticleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ArticlePage.Article article = mArticleList.get(position);
                String url = article.getLink();
                String title = article.getTitle();
                //携带Title和URL跳转到ArticleDetailActivity
                ArticleDetailActivity.actionStart(getActivity(), title, url);
            }

            @Override
            public void onCollectBtnClick(View view, int position) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }
}
