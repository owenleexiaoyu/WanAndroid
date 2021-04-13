package cc.lixiaoyu.wanandroid.core.projectdata;

import java.util.List;

import cc.lixiaoyu.wanandroid.base.BaseModel;
import cc.lixiaoyu.wanandroid.base.BasePresenter;
import cc.lixiaoyu.wanandroid.base.BaseView;
import cc.lixiaoyu.wanandroid.entity.Article;
import cc.lixiaoyu.wanandroid.entity.ProjectPage;
import cc.lixiaoyu.wanandroid.util.Optional;
import io.reactivex.Observable;

public interface ProjectDataContract {

    interface Model extends BaseModel{
        Observable<Optional<ProjectPage>> getProjectArticlesByCid(int page, String cid);
        Observable<Optional<String>> collectArticle(int articleId);
        Observable<Optional<String>> unCollectArticle(int articleId);
    }

    interface View extends BaseView{
        void showProjectArticlesByCid(List<Article> articleList);
        void showOpenArticleDetail(Article article);
        void showCollectArticle(boolean success, int position);
        void showCancelCollectArticle(boolean success, int position);
        void showLoadMoreProjectArticleByCid(List<Article> dataList, boolean success);
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
        public abstract void openArticleDetail(Article article);

        /**
         * 收藏文章
         * @param position
         * @param article
         */
        public abstract void collectArticle(int position, Article article);

        /**
         * 取消收藏文章
         * @param position
         * @param article
         */
        public abstract void cancelCollectArticle(int position, Article article);

    }
}
