package cc.lixiaoyu.wanandroid.core.home;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

import cc.lixiaoyu.wanandroid.entity.Article;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.util.Optional;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import io.reactivex.functions.Consumer;

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
        homeModel.getArticleList(mCurrentPage).subscribe(new Consumer<Optional<ArticlePage>>() {
            @Override
            public void accept(Optional<ArticlePage> articlePage) throws Exception {
                getView().showArticleList(articlePage.getIncludeNull().getArticleList());
            }
        });

    }

    @SuppressLint("CheckResult")
    @Override
    public void getTopArticles() {
        homeModel.getTopArticles()
                .subscribe(new Consumer<Optional<List<Article>>>() {
                    @Override
                    public void accept(Optional<List<Article>> articles) throws Exception {
                        getView().showTopArticles(articles.getIncludeNull());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showToast("获取置顶文章失败");
                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadMoreArticle() {
        mCurrentPage++;
        homeModel.getArticleList(mCurrentPage).subscribe(new Consumer<Optional<ArticlePage>>() {
            @Override
            public void accept(Optional<ArticlePage> articlePage) throws Exception {
                getView().showLoadMore(articlePage.getIncludeNull().getArticleList(), true);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                ToastUtil.showToast("获取文章列表数据失败");
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void getBannerData() {
        homeModel.getBannerData().subscribe(new Consumer<Optional<List<Banner>>>() {
            @Override
            public void accept(Optional<List<Banner>> banners) throws Exception {
                List<String> bannerTitles = new ArrayList<>();
                for (Banner banner : banners.getIncludeNull()) {
                    bannerTitles.add(banner.getTitle());
                }
                getView().showBannerData(banners.getIncludeNull(), bannerTitles);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                ToastUtil.showToast("获取Banner数据失败");
            }
        });
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
        homeModel.collectArticle(article.getId()).subscribe(new Consumer<Optional<String>>() {
            @Override
            public void accept(Optional<String> s) throws Exception {
                getView().showCollectArticle(true, position);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                getView().showCollectArticle(false, position);
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void cancelCollectArticle(final int position, Article article) {
        homeModel.unCollectArticle(article.getId()).subscribe(new Consumer<Optional<String>>() {
            @Override
            public void accept(Optional<String> s) throws Exception {
                getView().showCancelCollectArticle(true, position);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                getView().showCancelCollectArticle(false, position);
            }
        });
    }

    @Override
    public void start() {
        mCurrentPage = 0;
        getArticleList();
        getBannerData();
    }
}
