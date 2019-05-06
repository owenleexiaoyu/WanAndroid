package cc.lixiaoyu.wanandroid.core.search;

import java.util.List;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.HotKey;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.entity.WebSite;
import cc.lixiaoyu.wanandroid.util.BaseModelFactory;
import cc.lixiaoyu.wanandroid.util.Optional;
import cc.lixiaoyu.wanandroid.util.RetrofitHelper;
import io.reactivex.Observable;

public class SearchModel implements SearchContract.Model{
    private WanAndroidService mService;

    public SearchModel(){
        mService = RetrofitHelper.getInstance().getWanAndroidService();
    }


    @Override
    public Observable<Optional<List<HotKey>>> getHotKey() {
        return BaseModelFactory.compose(mService.getHotKey());
    }

    @Override
    public Observable<Optional<List<WebSite>>> getCommonSite() {
        return BaseModelFactory.compose(mService.getCommonSite());
    }
}
