package cc.lixiaoyu.wanandroid.core.home;

import java.util.ArrayList;
import java.util.List;

import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.util.Optional;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter extends HomeContract.Presenter {

    private HomeModel homeModel;

    private int mCurrentPage = 0;

    public HomePresenter(){
        this.homeModel = new HomeModel();
    }


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

    @Override
    public void getTopArticles() {
        homeModel.getTopArticles()
                .subscribe(new Consumer<Optional<List<ArticlePage.Article>>>() {
                    @Override
                    public void accept(Optional<List<ArticlePage.Article>> articles) throws Exception {
                        getView().showTopArticles(articles.getIncludeNull());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showToast("获取置顶文章失败");
                    }
                });
    }

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
    public void openArticleDetail(ArticlePage.Article article) {
        getView().showOpenArticleDetail(article);
    }

    @Override
    public void openBannerDetail(Banner banner) {
        getView().showOpenBannerDetail(banner);
    }

    @Override
    public void collectArticle(final int position, ArticlePage.Article article) {
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

    @Override
    public void cancelCollectArticle(final int position, ArticlePage.Article article) {
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
