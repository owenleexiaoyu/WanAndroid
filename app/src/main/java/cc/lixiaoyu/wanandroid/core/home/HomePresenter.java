package cc.lixiaoyu.wanandroid.core.home;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

import cc.lixiaoyu.wanandroid.entity.Article;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.util.ToastUtil;

public class HomePresenter extends HomeContract.Presenter {

    private HomeModel homeModel;

    private int mCurrentPage = 0;

    public HomePresenter(){
        this.homeModel = new HomeModel();
    }


    @SuppressLint("CheckResult")
    @Override
    public void getArticleList() {
        mCurrentPage = 0;
        getTopArticles();
        homeModel.getArticleList(mCurrentPage).subscribe(
                articlePage -> getView().showArticleList(articlePage.getIncludeNull().getArticleList()),
                throwable -> ToastUtil.showToast("获取文章列表失败")
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void getTopArticles() {
        homeModel.getTopArticles().subscribe(
                articles -> getView().showTopArticles(articles.getIncludeNull()),
                throwable -> ToastUtil.showToast("获取置顶文章失败")
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadMoreArticle() {
        mCurrentPage++;
        homeModel.getArticleList(mCurrentPage).subscribe(
                articlePage -> getView().showLoadMore(articlePage.getIncludeNull().getArticleList(), true),
                throwable -> ToastUtil.showToast("获取文章列表数据失败")
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void getBannerData() {
        homeModel.getBannerData().subscribe(
                banners -> {
                    List<String> bannerTitles = new ArrayList<>();
                    for (Banner banner : banners.getIncludeNull()) {
                        bannerTitles.add(banner.getTitle());
                    }
                    getView().showBannerData(banners.getIncludeNull(), bannerTitles);
                },
                throwable -> ToastUtil.showToast("获取Banner数据失败")
        );
    }

    @Override
    public void openArticleDetail(Article article) {
        getView().showOpenArticleDetail(article);
    }

    @Override
    public void openBannerDetail(Banner banner) {
        getView().showOpenBannerDetail(banner);
    }

    @SuppressLint("CheckResult")
    @Override
    public void collectArticle(final int position, Article article) {
        homeModel.collectArticle(article.getId()).subscribe(
                s -> getView().showCollectArticle(true, position),
                throwable -> getView().showCollectArticle(false, position)
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void cancelCollectArticle(final int position, Article article) {
        homeModel.unCollectArticle(article.getId()).subscribe(
                s -> getView().showCancelCollectArticle(true, position),
                throwable -> getView().showCancelCollectArticle(false, position)
        );
    }

    @Override
    public void start() {
        mCurrentPage = 0;
        getArticleList();
        getBannerData();
    }
}
