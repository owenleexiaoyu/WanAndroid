package cc.lixiaoyu.wanandroid.core.search;

import java.util.List;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.HotKey;
import cc.lixiaoyu.wanandroid.util.network.BaseModelFactory;
import cc.lixiaoyu.wanandroid.entity.Optional;
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager;
import io.reactivex.Observable;

public class SearchModel implements SearchContract.Model{
    private WanAndroidService mService;

    public SearchModel(){
        mService = RetrofitManager.getInstance().getWanAndroidService();
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
