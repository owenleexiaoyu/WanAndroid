package cc.lixiaoyu.wanandroid.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.util.GlideImageLoader;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.adapter.ArticleAdapter;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.ui.activity.ArticleDetailActivity;
import cc.lixiaoyu.wanandroid.util.RetrofitUtil;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    @BindView(R.id.fhome_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fhome_banner)
    com.youth.banner.Banner mBanner;
    @BindView(R.id.fhome_recyclerview)
    RecyclerView mRecyclerView;

    private ArticleAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<Banner> mBannerList;
    private List<String> mBannerTitleList;
    private List<ArticlePage.Article> mArticleList;
    private WanAndroidService mService;


    private int lastVisibleItem = 0;
    private int currentPage;   //当前加载了几页数据，同时也是上拉加载时传入的page
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container, false);
        ButterKnife.bind(this, view);
        mArticleList = new ArrayList<>();
        //获得Retrofit的Service对象
        mService = RetrofitUtil.getWanAndroidService();
        initBanner();
        getBannerData();
        getArtileList();

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
                ToastUtil.showToast("点击了收藏按钮");
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState ==RecyclerView.SCROLL_STATE_IDLE &&
                        lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    //上拉刷新
                    Log.d(TAG, "onScrollStateChanged: ----->加载更多");
                    loadMoreArticle();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getArtileList();
            }
        });
        return view;
    }

    private void loadMoreArticle() {
        Call<WanAndroidResult<ArticlePage>> call = mService.getArticleList(currentPage+"", null);
        call.enqueue(new Callback<WanAndroidResult<ArticlePage>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<ArticlePage>> call,
                                   Response<WanAndroidResult<ArticlePage>> response) {
                WanAndroidResult<ArticlePage> result = response.body();
                currentPage = result.getData().getCurPage();
                List<ArticlePage.Article> newList = result.getData().getArticleList();
                mAdapter.updateList(newList, true);
            }

            @Override
            public void onFailure(Call<WanAndroidResult<ArticlePage>> call, Throwable t) {
                ToastUtil.showToast("出错了");
            }
        });
    }

    /**
     * 初始化Banner相关的控件
     */
    private void initBanner() {
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        mBanner.setBannerAnimation(Transformer.DepthPage);
        mBanner.isAutoPlay(true);
        mBanner.setDelayTime(1500);
    }

    /**
     * 通过Retrofit发送Http请求，获取主页的文章列表
     */
    private void getArtileList() {
        mArticleList.clear();
        Call<WanAndroidResult<ArticlePage>> call = mService.getArticleList("0", null);
        call.enqueue(new Callback<WanAndroidResult<ArticlePage>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<ArticlePage>> call,
                                   Response<WanAndroidResult<ArticlePage>> response) {
                WanAndroidResult<ArticlePage> result = response.body();
                mArticleList.addAll(result.getData().getArticleList());
                currentPage = result.getData().getCurPage();
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<WanAndroidResult<ArticlePage>> call, Throwable t) {
                ToastUtil.showToast("出错了");
            }
        });
    }

    /**
     * 通过Retrofit发送Http请求，获取Banner信息
     */
    private void getBannerData() {
        mBannerTitleList = new ArrayList<>();
        Call<WanAndroidResult<List<Banner>>> call = mService.getBanner();
        call.enqueue(new Callback<WanAndroidResult<List<Banner>>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<List<Banner>>> call,
                                   Response<WanAndroidResult<List<Banner>>> response) {
                WanAndroidResult<List<Banner>> result = response.body();
                if(result.getErrorCode() == 0){
                    mBannerList = result.getData();
                    for(Banner banner : mBannerList){
                        mBannerTitleList.add(banner.getTitle());
                    }
                    mBanner.setImages(mBannerList);
                    mBanner.setBannerTitles(mBannerTitleList);
                    mBanner.setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            Banner banner = mBannerList.get(position);
                            //携带Title和URL跳转到ArticleDetailActivity
                            ArticleDetailActivity.actionStart(getActivity(),
                                    banner.getTitle(), banner.getUrl());
                        }
                    });
                    mBanner.start();
                }
            }

            @Override
            public void onFailure(Call<WanAndroidResult<List<Banner>>> call, Throwable t) {
                ToastUtil.showToast("出错了");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始自动轮播
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //停止自动轮播
        mBanner.stopAutoPlay();
    }
}
