package cc.lixiaoyu.wanandroid.core.collections;

import java.util.List;

import cc.lixiaoyu.wanandroid.base.BaseModel;
import cc.lixiaoyu.wanandroid.base.BasePresenter;
import cc.lixiaoyu.wanandroid.base.BaseView;
import cc.lixiaoyu.wanandroid.core.tree.KnowledgeTreeContract;
import cc.lixiaoyu.wanandroid.entity.CollectionPage;
import cc.lixiaoyu.wanandroid.util.Optional;
import io.reactivex.Observable;

public interface CollectionContract {
    interface Model extends BaseModel {
        Observable<Optional<CollectionPage>> getCollectionArticleList(int page);
        Observable<Optional<String>> collectArticle(int articleId);
        Observable<Optional<String>> unCollectArticle(int articleId);
    }

    interface View extends BaseView {
        void showCollectionArticleList(List<CollectionPage.CollectionArticle> articleList);
        void showLoadMoreCollectionArticle(boolean success, List<CollectionPage.CollectionArticle> articleList);
        void showUnCollectArticle(boolean success, int position);
        void showOpenArticleDetail(CollectionPage.CollectionArticle article);
    }

    abstract class  Presenter extends BasePresenter<CollectionContract.View>{
        public abstract void getCollectionArticleList();
        public abstract void loadMoreCollectionArticle();
        public abstract void unCollectArticle(int position, int articleId);
        public abstract void openArticleDetail(CollectionPage.CollectionArticle article);
    }
}
