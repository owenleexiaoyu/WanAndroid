package cc.lixiaoyu.wanandroid.mvp.model;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.ProjectPage;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.mvp.contract.ProjectDataContract;
import cc.lixiaoyu.wanandroid.util.RetrofitUtil;
import retrofit2.Call;

public class ProjectDataModel implements ProjectDataContract.Model {
    private WanAndroidService mService;
    public ProjectDataModel(){
        mService = RetrofitUtil.getWanAndroidService();
    }

    @Override
    public Call<WanAndroidResult<ProjectPage>> getProjectArticlesByCid(int page, String cid) {
        return mService.getProjectArticlesByCid(page, cid);
    }
}
