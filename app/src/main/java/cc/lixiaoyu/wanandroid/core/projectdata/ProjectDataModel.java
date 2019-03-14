package cc.lixiaoyu.wanandroid.core.projectdata;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.ProjectPage;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.core.projectdata.ProjectDataContract;
import cc.lixiaoyu.wanandroid.util.RetrofitHelper;
import retrofit2.Call;

public class ProjectDataModel implements ProjectDataContract.Model {
    private WanAndroidService mService;
    public ProjectDataModel(){
        mService = RetrofitHelper.getInstance().getWanAndroidService();
    }

    @Override
    public Call<WanAndroidResult<ProjectPage>> getProjectArticlesByCid(int page, String cid) {
        return mService.getProjectArticlesByCid(page, cid);
    }
}
