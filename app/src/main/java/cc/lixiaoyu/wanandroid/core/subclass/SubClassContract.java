package cc.lixiaoyu.wanandroid.core.subclass;


import java.util.List;

import cc.lixiaoyu.wanandroid.base.mvp.BaseModel;
import cc.lixiaoyu.wanandroid.base.mvp.BasePresenter;
import cc.lixiaoyu.wanandroid.base.mvp.BaseView;
import cc.lixiaoyu.wanandroid.entity.Article;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.Optional;
import io.reactivex.Observable;

public interface SubClassContract {

    interface Model extends BaseModel{
        Observable<Optional<ArticlePage>> getArticleListByCid(int page, String cid);
        Observable<Optional<String>> collectArticle(int articleId);
        Observable<Optional<String>> unCollectArticle(int articleId);
    }

    interface View extends BaseView{
        void showArticleListByCid(List<Article> articles);
        void showOpenArticleDetail(Article article);
        void showCollectArticle(boolean success, int position);
        void showCancelCollectArticle(boolean success, int position);
        void showLoadMoreArticleByCid(List<Article> articles, boolean success);
    }

    abstract class Presenter extends BasePresenter<View>{
        /**
         * 获取一个二级分类下的所有文章，分页加载
         * @param cid  二级分类的id
         */
        public abstract void getArticleListByCid(String cid);

        /**
         * 加载二级分类下更多的文章
         * @param cid
         */
        public abstract void loadMoreArticleByCid(String cid);

        /**
         * 打开文章详情页
         * @param article
         */
        public abstract void openArticleDetail(Article article);

        /**
         * 收藏文章
         * @param article
         */
        public abstract void collectArticle(int position, Article article);

        /**
         * 取消收藏文章
         * @param article
         */
        public abstract void cancelCollectArticle(int position, Article article);
    }
}
