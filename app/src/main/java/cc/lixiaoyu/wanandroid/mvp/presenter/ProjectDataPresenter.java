package cc.lixiaoyu.wanandroid.mvp.presenter;

import java.util.List;

import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.ProjectPage;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.mvp.contract.ProjectDataContract;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectDataPresenter implements ProjectDataContract.Presenter {
    private ProjectDataContract.View mView;
    private ProjectDataContract.Model mModel;

    //加载的当前页,默认为1
    private int mCurrentPage = 1;

    public ProjectDataPresenter(ProjectDataContract.View view,
                                ProjectDataContract.Model model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
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
                    mView.showProjectArticlesByCid(result.getData().getDataList());
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
                    mView.showLoadMoreProjectArticleByCid(newList,true);
                }
            }

            @Override
            public void onFailure(Call<WanAndroidResult<ProjectPage>> call, Throwable t) {
                mView.showLoadMoreProjectArticleByCid(null,false);
            }
        });
    }

    @Override
    public void openArticleDetail(ProjectPage.ProjectData article) {
        mView.showOpenArticleDetail(article);
    }

    @Override
    public void collectArticle(ProjectPage.ProjectData article) {

    }

    @Override
    public void cancelCollectArticle(ProjectPage.ProjectData article) {

    }

    @Override
    public void start(String cid) {
        mView.showRefreshing();
        getProjectArticlesByCid(cid);
    }

    @Override
    public void start() {

    }
}
