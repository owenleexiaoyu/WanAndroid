package cc.lixiaoyu.wanandroid.core.projectdata;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.ProjectPage;
import cc.lixiaoyu.wanandroid.util.network.BaseModelFactory;
import cc.lixiaoyu.wanandroid.entity.Optional;
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager;
import io.reactivex.Observable;

public class ProjectDataModel implements ProjectDataContract.Model {
    private final WanAndroidService mService;
    public ProjectDataModel(){
        mService = RetrofitManager.INSTANCE.getWanAndroidService();
    }

    @Override
    public Observable<Optional<ProjectPage>> getProjectArticlesByCid(int page, String cid) {
        return BaseModelFactory.compose(mService.getProjectArticlesByCid(page, cid));
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
