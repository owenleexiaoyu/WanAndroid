package cc.lixiaoyu.wanandroid.mvp.model;

import java.util.List;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.mvp.contract.HomeContract;
import cc.lixiaoyu.wanandroid.util.RetrofitUtil;
import retrofit2.Call;

public class HomeModel implements HomeContract.Model{

    private WanAndroidService mService;

    public HomeModel(){
        mService = RetrofitUtil.getWanAndroidService();
    }

    @Override
    public Call<WanAndroidResult<ArticlePage>> getArticleList(int page) {
        return mService.getArticleList(page, null);
    }

    @Override
    public Call<WanAndroidResult<List<Banner>>> getBannerData() {
        return mService.getBannerData();
    }
}
