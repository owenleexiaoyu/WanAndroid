package cc.lixiaoyu.wanandroid.core.collection;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.CollectionPage;
import cc.lixiaoyu.wanandroid.util.network.BaseModelFactory;
import cc.lixiaoyu.wanandroid.entity.Optional;
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager;
import io.reactivex.Observable;

public class CollectionModel implements CollectionContract.Model {
    private WanAndroidService mService;

    public CollectionModel(){
        mService = RetrofitManager.getInstance().getWanAndroidService();
    }

    @Override
    public Observable<Optional<CollectionPage>> getCollectionArticleList(int page) {
        return BaseModelFactory.compose(mService.getCollectionArticleList(page));
    }

    @Override
    public Observable<Optional<String>> collectArticle(int articleId) {
        return BaseModelFactory.compose(mService.collectArticle(articleId));
    }

    @Override
    public Observable<Optional<String>> unCollectArticle(int articleId) {
        return BaseModelFactory.compose(mService.unCollectArticleFromCollectionPage(articleId, -1));
    }
}
