package cc.lixiaoyu.wanandroid.core.projectdata;

import java.util.List;

import cc.lixiaoyu.wanandroid.base.BaseModel;
import cc.lixiaoyu.wanandroid.base.BasePresenter;
import cc.lixiaoyu.wanandroid.base.BaseView;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.ProjectPage;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.util.Optional;
import io.reactivex.Observable;
import retrofit2.Call;

public interface ProjectDataContract {

    interface Model extends BaseModel{
        Observable<Optional<ProjectPage>> getProjectArticlesByCid(int page, String cid);
        Observable<Optional<String>> collectArticle(int articleId);
        Observable<Optional<String>> unCollectArticle(int articleId);
    }

    interface View extends BaseView{
        void showProjectArticlesByCid(List<ProjectPage.ProjectData> dataList);
        void showOpenArticleDetail(ProjectPage.ProjectData data);
        void showCollectArticle(boolean success, int position);
        void showCancelCollectArticle(boolean success, int position);
        void showLoadMoreProjectArticleByCid(List<ProjectPage.ProjectData> dataList, boolean success);
    }

    abstract class Presenter extends BasePresenter<View>{
        /**
         * 获取项目分类下的所有文章，分页加载
         * @param cid  二级分类的id
         */
        public abstract void getProjectArticlesByCid(String cid);

        /**
         * 加载项目分类下更多的文章
         * @param cid
         */
        public abstract void loadMoreProjectArticlesByCid(String cid);

        /**
         * 打开文章详情页
         * @param article
         */
        public abstract void openArticleDetail(ProjectPage.ProjectData article);

        /**
         * 收藏文章
         * @param position
         * @param article
         */
        public abstract void collectArticle(int position, ProjectPage.ProjectData article);

        /**
         * 取消收藏文章
         * @param position
         * @param article
         */
        public abstract void cancelCollectArticle(int position, ProjectPage.ProjectData article);

    }
}
