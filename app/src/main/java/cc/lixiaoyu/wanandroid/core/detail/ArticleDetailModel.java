package cc.lixiaoyu.wanandroid.core.detail;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.util.network.BaseModelFactory;
import cc.lixiaoyu.wanandroid.entity.Optional;
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager;
import io.reactivex.Observable;

public class ArticleDetailModel implements ArticleDetailContract.Model {

    private WanAndroidService mService = RetrofitManager.getInstance().getWanAndroidService();

    @Override
    public Observable<Optional<String>> collectArticle(int articleId) {
        return BaseModelFactory.compose(mService.collectArticle(articleId));
    }

    @Override
    public Observable<Optional<String>> unCollectArticle(int articleId) {
        return BaseModelFactory.compose(mService.unCollectArticleFromArticleList(articleId));
    }
}
