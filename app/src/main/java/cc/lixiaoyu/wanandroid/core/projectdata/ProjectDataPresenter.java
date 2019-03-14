package cc.lixiaoyu.wanandroid.core.projectdata;

import java.util.List;

import cc.lixiaoyu.wanandroid.entity.ProjectPage;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.core.projectdata.ProjectDataContract;
import cc.lixiaoyu.wanandroid.core.projectdata.ProjectDataModel;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectDataPresenter extends ProjectDataContract.Presenter {

    private ProjectDataContract.Model mModel;

    //加载的当前页,默认为1
    private int mCurrentPage = 1;

    public ProjectDataPresenter(){
        mModel = new ProjectDataModel();
    }

    @Override
    public void getProjectArticlesByCid(String cid) {
        mCurrentPage = 1;
        Call<WanAndroidResult<ProjectPage>> call = mModel.getProjectArticlesByCid(mCurrentPage, cid);
        call.enqueue(new Callback<WanAndroidResult<ProjectPage>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<ProjectPage>> call,
                                   Response<WanAndroidResult<ProjectPage>> response) {
                WanAndroidResult<ProjectPage> result = response.body();
                if(result.getErrorCode() == 0){
                    getView().showProjectArticlesByCid(result.getData().getDataList());
                }
            }

            @Override
            public void onFailure(Call<WanAndroidResult<ProjectPage>> call, Throwable t) {
                ToastUtil.showToast("出错了");
                t.printStackTrace();
            }
        });
    }

    @Override
    public void loadMoreProjectArticlesByCid(String cid) {
        mCurrentPage++;
        Call<WanAndroidResult<ProjectPage>> call = mModel.getProjectArticlesByCid(mCurrentPage, cid);
        call.enqueue(new Callback<WanAndroidResult<ProjectPage>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<ProjectPage>> call,
                                   Response<WanAndroidResult<ProjectPage>> response) {
                WanAndroidResult<ProjectPage> result = response.body();
                if(result.getErrorCode() == 0){
                    List<ProjectPage.ProjectData> newList = result.getData().getDataList();
                    getView().showLoadMoreProjectArticleByCid(newList,true);
                }
            }

            @Override
            public void onFailure(Call<WanAndroidResult<ProjectPage>> call, Throwable t) {
                getView().showLoadMoreProjectArticleByCid(null,false);
            }
        });
    }

    @Override
    public void openArticleDetail(ProjectPage.ProjectData article) {
        getView().showOpenArticleDetail(article);
    }

    @Override
    public void collectArticle(ProjectPage.ProjectData article) {

    }

    @Override
    public void cancelCollectArticle(ProjectPage.ProjectData article) {

    }

    @Override
    public void start() {

    }
}
