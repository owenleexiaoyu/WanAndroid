package cc.lixiaoyu.wanandroid.core.home;


import java.util.List;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.Article;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.util.BaseModelFactory;
import cc.lixiaoyu.wanandroid.util.Optional;
import cc.lixiaoyu.wanandroid.util.RetrofitHelper;
import io.reactivex.Observable;

public class HomeModel implements HomeContract.Model{

    private WanAndroidService mService;

    public HomeModel(){
        mService = RetrofitHelper.getInstance().getWanAndroidService();
    }

    @Override
    public Observable<Optional<ArticlePage>> getArticleList(int page) {
        return BaseModelFactory.compose(mService.getArticleList(page, null));
    }

    @Override
    public Observable<Optional<List<Banner>>> getBannerData() {
        return BaseModelFactory.compose(mService.getBannerData());
    }

    @Override
    public Observable<Optional<List<Article>>> getTopArticles() {
        return BaseModelFactory.compose(mService.getTopArticles());
    }

    @Override
    public Observable<Optional<String>> collectArticle(int articleId) {
        return BaseModelFactory.compose(mService.collectArticle(articleId));
    }

    @Override
    public Observable<Optional<String>> unCollectArticle(int articleId) {
        return BaseModelFactory.compose(mService.unCollectArticleFromArticleList(articleId));
    }
}
