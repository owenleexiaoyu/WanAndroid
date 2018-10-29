package cc.lixiaoyu.wanandroid.mvp.presenter;

import java.util.ArrayList;
import java.util.List;

import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.mvp.contract.HomeContract;
import cc.lixiaoyu.wanandroid.mvp.model.HomeModel;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter implements HomeContract.Presenter {

    private HomeModel homeModel;
    private HomeContract.View homeView;

    private int mCurrentPage = 0;

    public HomePresenter(HomeContract.View homeView, HomeModel homeModel){
        this.homeModel = homeModel;
        this.homeView = homeView;
        this.homeView.setPresenter(this);
    }


    @Override
    public void getArticleList() {
        mCurrentPage = 0;
        Call<WanAndroidResult<ArticlePage>> call = homeModel.getArticleList(mCurrentPage);
        call.enqueue(new Callback<WanAndroidResult<ArticlePage>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<ArticlePage>> call,
                                   Response<WanAndroidResult<ArticlePage>> response) {
                WanAndroidResult<ArticlePage> result = response.body();
                List<ArticlePage.Article> articles = result.getData().getArticleList();
                homeView.showArticleList(articles);
            }

            @Override
            public void onFailure(Call<WanAndroidResult<ArticlePage>> call, Throwable t) {
                ToastUtil.showToast("出错了");
            }
        });
    }

    @Override
    public void loadMoreArticle() {
        mCurrentPage++;
        Call<WanAndroidResult<ArticlePage>> call = homeModel.getArticleList(mCurrentPage);
        call.enqueue(new Callback<WanAndroidResult<ArticlePage>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<ArticlePage>> call,
                                   Response<WanAndroidResult<ArticlePage>> response) {
                WanAndroidResult<ArticlePage> result = response.body();
                List<ArticlePage.Article> newList = result.getData().getArticleList();
                homeView.showLoadMore(newList, true);
            }

            @Override
            public void onFailure(Call<WanAndroidResult<ArticlePage>> call, Throwable t) {
                homeView.showLoadMore(null, false);
            }
        });
    }

    @Override
    public void getBannerData() {
        Call<WanAndroidResult<List<Banner>>> call = homeModel.getBannerData();
        call.enqueue(new Callback<WanAndroidResult<List<Banner>>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<List<Banner>>> call,
                                   Response<WanAndroidResult<List<Banner>>> response) {
                WanAndroidResult<List<Banner>> result = response.body();
                if (result.getErrorCode() == 0) {
                    List<Banner> banners = result.getData();
                    List<String> bannerTitles = new ArrayList<>();
                    for(Banner banner : banners){
                        bannerTitles.add(banner.getTitle());
                    }
                    homeView.showBannerData(banners,bannerTitles);
                }
            }

            @Override
            public void onFailure(Call<WanAndroidResult<List<Banner>>> call, Throwable t) {
                ToastUtil.showToast("出错了");
            }
        });
    }

    @Override
    public void openArticleDetail(ArticlePage.Article article) {
        homeView.showOpenArticleDetail(article);
    }

    @Override
    public void openBannerDetail(Banner banner) {
        homeView.showOpenBannerDetail(banner);
    }

    @Override
    public void collectArticle(int position, ArticlePage.Article article) {

    }

    @Override
    public void cancelCollectArticle(int position, ArticlePage.Article article) {

    }

    @Override
    public void start() {
        mCurrentPage = 0;
        getArticleList();
        getBannerData();
    }
}
