package cc.lixiaoyu.wanandroid.core.collection;

import java.util.List;

import cc.lixiaoyu.wanandroid.base.mvp.BaseModel;
import cc.lixiaoyu.wanandroid.base.mvp.BasePresenter;
import cc.lixiaoyu.wanandroid.base.mvp.BaseView;
import cc.lixiaoyu.wanandroid.entity.Article;
import cc.lixiaoyu.wanandroid.entity.CollectionPage;
import cc.lixiaoyu.wanandroid.entity.Optional;
import io.reactivex.Observable;

public interface CollectionContract {
    interface Model extends BaseModel {
        Observable<Optional<CollectionPage>> getCollectionArticleList(int page);

        Observable<Optional<String>> collectArticle(int articleId);

        Observable<Optional<String>> unCollectArticle(int articleId);
    }

    interface View extends BaseView {
        void showCollectionArticleList(List<Article> articleList);

        void showLoadMoreCollectionArticle(boolean success, List<Article> articleList);

        void showUnCollectArticle(boolean success, int position);

        void showOpenArticleDetail(Article article);
    }

    abstract class Presenter extends BasePresenter<CollectionContract.View> {
        public abstract void getCollectionArticleList();

        public abstract void loadMoreCollectionArticle();

        public abstract void unCollectArticle(int position, int articleId);

        public abstract void openArticleDetail(Article article);
    }
}
