package cc.lixiaoyu.wanandroid.core.wechat;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.WechatPage;
import cc.lixiaoyu.wanandroid.util.network.BaseModelFactory;
import cc.lixiaoyu.wanandroid.entity.Optional;
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager;
import io.reactivex.Observable;

public class WechatDataModel implements WechatDataContract.Model {
    private WanAndroidService mService;
    public WechatDataModel(){
        mService = RetrofitManager.getInstance().getWanAndroidService();
    }

    @Override
    public Observable<Optional<WechatPage>> getWechatArticlesById(int id, int page) {
        return BaseModelFactory.compose(mService.getWechatPublicArticlesById(page, id));
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
