package cc.lixiaoyu.wanandroid.core.home;

import java.util.ArrayList;
import java.util.List;

import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
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
        homeModel.getArticleList(mCurrentPage).subscribe(new Consumer<ArticlePage>() {
            @Override
            public void accept(ArticlePage articlePage) throws Exception {
                getView().showArticleList(articlePage.getArticleList());
            }
        });

    }

    @Override
    public void getTopArticles() {
        homeModel.getTopArticles()
                .subscribe(new Consumer<List<ArticlePage.Article>>() {
            @Override
            public void accept(List<ArticlePage.Article> articles) throws Exception {
                getView().showTopArticles(articles);
            }
        });
    }

    @Override
    public void loadMoreArticle() {
        mCurrentPage++;
        homeModel.getArticleList(mCurrentPage).subscribe(new Consumer<ArticlePage>() {
            @Override
            public void accept(ArticlePage articlePage) throws Exception {
                getView().showLoadMore(articlePage.getArticleList(), true);
            }
        });
    }

    @Override
    public void getBannerData() {
        homeModel.getBannerData().subscribe(new Consumer<List<Banner>>() {
            @Override
            public void accept(List<Banner> banners) throws Exception {
                List<String> bannerTitles = new ArrayList<>();
                for(Banner banner : banners){
                    bannerTitles.add(banner.getTitle());
                }
                getView().showBannerData(banners,bannerTitles);
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
