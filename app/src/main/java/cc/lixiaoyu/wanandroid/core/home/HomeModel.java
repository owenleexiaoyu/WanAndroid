package cc.lixiaoyu.wanandroid.core.home;


import java.util.List;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.util.BaseModelFactory;
import cc.lixiaoyu.wanandroid.util.RetrofitHelper;
import io.reactivex.Observable;

public class HomeModel implements HomeContract.Model{

    private WanAndroidService mService;

    public HomeModel(){
        mService = RetrofitHelper.getInstance().getWanAndroidService();
    }

    @Override
    public Observable<ArticlePage> getArticleList(int page) {
        return BaseModelFactory.compose(mService.getArticleList(page, null));
    }

    @Override
    public Observable<List<Banner>> getBannerData() {
        return BaseModelFactory.compose(mService.getBannerData());
    }

    @Override
    public Observable<List<ArticlePage.Article>> getTopArticles() {
        return BaseModelFactory.compose(mService.getTopArticles());
    }
}
