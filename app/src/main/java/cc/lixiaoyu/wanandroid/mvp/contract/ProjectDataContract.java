package cc.lixiaoyu.wanandroid.mvp.contract;

import java.util.List;

import cc.lixiaoyu.wanandroid.base.BaseModel;
import cc.lixiaoyu.wanandroid.base.BasePresenter;
import cc.lixiaoyu.wanandroid.base.BaseView;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.ProjectPage;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import retrofit2.Call;

public interface ProjectDataContract {

    interface Model extends BaseModel{
        Call<WanAndroidResult<ProjectPage>> getProjectArticlesByCid(int page, String cid);
    }

    interface View extends BaseView<Presenter>{
        void showProjectArticlesByCid(List<ProjectPage.ProjectData> dataList);
        void showOpenArticleDetail(ProjectPage.ProjectData data);
        void showCollectArticle(ProjectPage.ProjectData article);
        void showCancelCollectArticle(ProjectPage.ProjectData article);
        void showRefreshing();
        void completeRefresh();
        void showLoadMoreProjectArticleByCid(List<ProjectPage.ProjectData> dataList, boolean success);
    }

    interface Presenter extends BasePresenter{
        /**
         * 获取项目分类下的所有文章，分页加载
         * @param cid  二级分类的id
         */
        void getProjectArticlesByCid(String cid);

        /**
         * 加载项目分类下更多的文章
         * @param cid
         */
        void loadMoreProjectArticlesByCid(String cid);

        /**
         * 打开文章详情页
         * @param article
         */
        void openArticleDetail(ProjectPage.ProjectData article);

        /**
         * 收藏文章
         * @param article
         */
        void collectArticle(ProjectPage.ProjectData article);

        /**
         * 取消收藏文章
         * @param article
         */
        void cancelCollectArticle(ProjectPage.ProjectData article);

        /**
         * 开始
         * @param cid
         */
        void start(String cid);
    }
}
