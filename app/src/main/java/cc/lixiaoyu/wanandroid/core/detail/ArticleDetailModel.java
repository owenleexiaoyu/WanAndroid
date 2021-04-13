package cc.lixiaoyu.wanandroid.core.detail;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.util.BaseModelFactory;
import cc.lixiaoyu.wanandroid.util.Optional;
import cc.lixiaoyu.wanandroid.util.RetrofitHelper;
import io.reactivex.Observable;

public class ArticleDetailModel implements ArticleDetailContract.Model {

    private WanAndroidService mService = RetrofitHelper.getInstance().getWanAndroidService();

    @Override
    public Observable<Optional<String>> collectArticle(int articleId) {
        return BaseModelFactory.compose(mService.collectArticle(articleId));
    }

    @Override
    public Observable<Optional<String>> unCollectArticle(int articleId) {
        return BaseModelFactory.compose(mService.unCollectArticleFromArticleList(articleId));
    }
}
