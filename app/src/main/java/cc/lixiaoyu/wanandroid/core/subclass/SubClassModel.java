package cc.lixiaoyu.wanandroid.core.subclass;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.util.BaseModelFactory;
import cc.lixiaoyu.wanandroid.util.RetrofitHelper;
import io.reactivex.Observable;
import retrofit2.Call;

public class SubClassModel implements SubClassContract.Model{

    private WanAndroidService mService;

    public SubClassModel(){
        mService = RetrofitHelper.getInstance().getWanAndroidService();
    }

    @Override
    public Observable<ArticlePage> getArticleListByCid(int page, String cid) {
        return BaseModelFactory.compose(mService.getArticleList(page, cid));
    }
}
