package cc.lixiaoyu.wanandroid.core.home;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.lixiaoyu.wanandroid.base.mvp.MVPBaseFragment;
import cc.lixiaoyu.wanandroid.core.account.ui.LoginActivity;
import cc.lixiaoyu.wanandroid.entity.Article;
import cc.lixiaoyu.wanandroid.util.GlideImageLoader;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.adapter.ArticleAdapter;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.core.detail.ArticleDetailActivity;
import cc.lixiaoyu.wanandroid.util.ToastUtil;

public class HomeFragment extends MVPBaseFragment<HomeContract.Presenter> implements HomeContract.View {
    private static final String TAG = "HomeFragment";

    @BindView(R.id.fhome_smart_refresh)
    SmartRefreshLayout mSmartRefreshLayout;
    com.youth.banner.Banner mBanner;
    @BindView(R.id.fhome_recyclerview)
    RecyclerView mRecyclerView;

    private ArticleAdapter mAdapter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected void initData() {
        mPresenter.start();
    }

    @Override
    protected void initView(View view) {

        //初始化Adapter
        mAdapter = new ArticleAdapter(R.layout.item_recyclerview_article,
                new ArrayList<Article>(0));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Article article = mAdapter.getData().get(position);
                mPresenter.openArticleDetail(article);
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUtil.showToast("点击了收藏按钮");
                //判断是否用户登陆
                if (!mPresenter.getDataManager().getLoginState()) {
                    //未登录，前往登陆页面进行登陆操作
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    //登陆后，可以进行文章的收藏与取消收藏操作
                    //如果文章已经被收藏了，就取消收藏，如果没有收藏，就收藏
                    if (mAdapter.getItem(position).isCollect()) {
                        mPresenter.cancelCollectArticle(position, mAdapter.getItem(position));
                    } else {
                        mPresenter.collectArticle(position, mAdapter.getItem(position));
                    }
                }

            }
        });

        //在RecyclerView中添加Banner的HeaderView
        LinearLayout linearLayout = (LinearLayout) LayoutInflater
                .from(getActivity()).inflate(R.layout.layout_banner, null);
        mBanner = linearLayout.findViewById(R.id.fhome_banner);
        initBanner();
        //必须先将Banner的视图从原来的父视图中删除，才能添加到RecyclerView中
        linearLayout.removeView(mBanner);
        mAdapter.setHeaderView(mBanner);

        //设置RecyclerView的Adapter和布局管理器
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //设置SmartRefreshLayout的Header和Footer
        mSmartRefreshLayout.setRefreshHeader(new MaterialHeader(getActivity()));
        mSmartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        //设置下拉刷新和上拉加载更多的监听器
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.getArticleList();
            }
        });
        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                mPresenter.loadMoreArticle();
            }
        });
    }

    @Override
    protected int attachLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void showArticleList(List<Article> articles) {
        mAdapter.addData(articles);
        mSmartRefreshLayout.finishRefresh();
    }

    @Override
    public void showTopArticles(List<Article> articles) {
        mAdapter.replaceData(articles);
    }

    /**
     * 初始化Banner控件相关属性
     */
    private void initBanner() {
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        mBanner.setBannerAnimation(Transformer.DepthPage);
        mBanner.isAutoPlay(true);
        mBanner.setDelayTime(1500);
    }

    @Override
    public void showBannerData(final List<Banner> banners, List<String> bannerTitleList) {

        mBanner.setImages(banners);
        mBanner.setBannerTitles(bannerTitleList);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Banner banner = banners.get(position);
                mPresenter.openBannerDetail(banner);
            }
        });
        mBanner.start();
    }

    @Override
    public void showCollectArticle(boolean success, int position) {
        if (success) {
            ToastUtil.showToast("收藏文章成功");
            mAdapter.getData().get(position).setCollect(true);
            mAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showToast("收藏文章失败");
        }
    }

    @Override
    public void showCancelCollectArticle(boolean success, int position) {
        if (success) {
            ToastUtil.showToast("取消收藏文章成功");
            mAdapter.getData().get(position).setCollect(false);
            mAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showToast("取消收藏文章失败");
        }
    }

    @Override
    public void showLoadMore(List<Article> articles, boolean success) {
        //如果成功
        if (success) {
            mAdapter.addData(articles);
            mSmartRefreshLayout.finishLoadMore();
        }
        //如果失败
        else {
            mSmartRefreshLayout.finishLoadMore(false);
        }
    }

    @Override
    public void showOpenArticleDetail(Article article) {
        ArticleDetailActivity.actionStart(getActivity(), article.toDetailParam());
    }

    @Override
    public void showOpenBannerDetail(Banner banner) {
        ArticleDetailActivity.actionStart(getActivity(), banner.toDetailParam());
    }

    /**
     * 回到列表的顶部
     */
    public void jumpToListTop() {
        mRecyclerView.smoothScrollToPosition(0);
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

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected HomeContract.Presenter createPresenter() {
        return new HomePresenter();
    }
}
