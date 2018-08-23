package cc.lixiaoyu.wanandroid.mvp.model;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.mvp.contract.SubClassContract;
import cc.lixiaoyu.wanandroid.util.RetrofitUtil;
import retrofit2.Call;

public class SubClassModel implements SubClassContract.Model{

    private WanAndroidService mService;

    public SubClassModel(){
        mService = RetrofitUtil.getWanAndroidService();
    }

    @Override
    public Call<WanAndroidResult<ArticlePage>> getArticleListByCid(int page, String cid) {
        return mService.getArticleList(page, cid);
    }
}
