package cc.lixiaoyu.wanandroid.core.projectdata;

import java.util.List;

import cc.lixiaoyu.wanandroid.entity.ProjectPage;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.core.projectdata.ProjectDataContract;
import cc.lixiaoyu.wanandroid.core.projectdata.ProjectDataModel;
import cc.lixiaoyu.wanandroid.util.Optional;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import io.reactivex.functions.Consumer;
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
        mModel.getProjectArticlesByCid(mCurrentPage, cid)
                .subscribe(new Consumer<Optional<ProjectPage>>() {
                    @Override
                    public void accept(Optional<ProjectPage> projectPageOptional) throws Exception {
                        getView().showProjectArticlesByCid(projectPageOptional.getIncludeNull().getDataList());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable t) throws Exception {
                        ToastUtil.showToast("出错了");
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void loadMoreProjectArticlesByCid(String cid) {
        mCurrentPage++;
        mModel.getProjectArticlesByCid(mCurrentPage, cid)
                .subscribe(new Consumer<Optional<ProjectPage>>() {
                    @Override
                    public void accept(Optional<ProjectPage> projectPageOptional) throws Exception {
                        getView().showLoadMoreProjectArticleByCid(projectPageOptional.getIncludeNull().getDataList(),
                                true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().showLoadMoreProjectArticleByCid(null,false);
                    }
                });
    }

    @Override
    public void openArticleDetail(ProjectPage.ProjectData article) {
        getView().showOpenArticleDetail(article);
    }

    @Override
    public void collectArticle(final int position, ProjectPage.ProjectData article) {
        mModel.collectArticle(article.getId()).subscribe(new Consumer<Optional<String>>() {
            @Override
            public void accept(Optional<String> s) throws Exception {
                getView().showCollectArticle(true, position);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                getView().showCollectArticle(false, position);
            }
        });
    }

    @Override
    public void cancelCollectArticle(final int position, ProjectPage.ProjectData article) {
        mModel.unCollectArticle(article.getId()).subscribe(new Consumer<Optional<String>>() {
            @Override
            public void accept(Optional<String> s) throws Exception {
                getView().showCancelCollectArticle(true, position);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                getView().showCancelCollectArticle(false, position);
            }
        });
    }

    @Override
    public void start() {

    }
}
