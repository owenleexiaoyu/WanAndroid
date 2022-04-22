package cc.lixiaoyu.wanandroid.core.subclass;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.util.network.BaseModelFactory;
import cc.lixiaoyu.wanandroid.entity.Optional;
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager;
import io.reactivex.Observable;

public class SubClassModel implements SubClassContract.Model{

    private WanAndroidService mService;

    public SubClassModel(){
        mService = RetrofitManager.getInstance().getWanAndroidService();
    }

    @Override
    public Observable<Optional<ArticlePage>> getArticleListByCid(int page, String cid) {
        return BaseModelFactory.compose(mService.getArticleList(page, cid));
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
