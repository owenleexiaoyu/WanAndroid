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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.zhengsr.viewpagerlib.anim.DepthPageTransformer;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.ZoomIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.adapter.ArticleAdapter;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.service.WanAndroidService;
import cc.lixiaoyu.wanandroid.util.AppConst;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    @BindView(R.id.fhome_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fhome_banner)
    BannerViewPager mBannerViewPager;
    @BindView(R.id.fhome_zoom_indicator)
    ZoomIndicator mZoomIndicator;
    @BindView(R.id.fhome_recyclerview)
    RecyclerView mRecyclerView;

    private ArticleAdapter mAdapter;
    private List<Banner> mBannerList;
    private List<ArticlePage.Article> mArticleList;
    private Gson mGson;
    private Retrofit mRetrofit;
    private WanAndroidService mService;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                PageBean pageBean = new PageBean.Builder<Banner>()
                        .setDataObjects(mBannerList)
                        .setIndicator(mZoomIndicator)
                        .builder();
                mBannerViewPager.setPageListener(pageBean,
                        R.layout.item_banner, new PageHelperListener() {
                    @Override
                    public void getItemView(View view, Object o) {
                        Banner data = (Banner) o;
                        ImageView imageView = view.findViewById(R.id.banner_item_img);
                        TextView textView = view.findViewById(R.id.banner_item_text);
                        textView.setText(data.getTitle());
                        RequestOptions options = new RequestOptions()
                                .centerCrop();
                        Glide.with(getActivity())
                                .load(data.getImagePath())
                                .apply(options)
                                .into(imageView);
                    }
                });
                mBannerViewPager.setPageTransformer(false, new DepthPageTransformer());
            }
            else if(msg.what == 2){
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container, false);
        ButterKnife.bind(this, view);
        mArticleList = new ArrayList<>();
        initRetrofit();
        getBanner();
        getArtileList();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ArticleAdapter(getActivity(), mArticleList);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getArtileList();
            }
        });
        return view;
    }

    private void initRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(AppConst.WANANDROID_BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = mRetrofit.create(WanAndroidService.class);
        mGson = new Gson();
    }

    private void getArtileList() {
        mArticleList.clear();
        Call<WanAndroidResult<ArticlePage>> call = mService.getArticleList("0");
        call.enqueue(new Callback<WanAndroidResult<ArticlePage>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<ArticlePage>> call,
                                   Response<WanAndroidResult<ArticlePage>> response) {
                WanAndroidResult<ArticlePage> result = response.body();
                mArticleList.addAll(result.getData().getArticleList());
                handler.sendEmptyMessage(2);
            }

            @Override
            public void onFailure(Call<WanAndroidResult<ArticlePage>> call, Throwable t) {
                Toast.makeText(getActivity(), "出错了", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void getBanner() {

        Call<WanAndroidResult<List<Banner>>> call = mService.getBanner();
        call.enqueue(new Callback<WanAndroidResult<List<Banner>>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<List<Banner>>> call,
                                   Response<WanAndroidResult<List<Banner>>> response) {
                WanAndroidResult<List<Banner>> result = response.body();
                if(result.getErrorCode() == 0){
                    mBannerList = result.getData();
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onFailure(Call<WanAndroidResult<List<Banner>>> call, Throwable t) {
                Toast.makeText(getActivity(), "出错了", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
