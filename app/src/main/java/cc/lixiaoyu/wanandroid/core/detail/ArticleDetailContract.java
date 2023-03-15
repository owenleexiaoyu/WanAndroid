package cc.lixiaoyu.wanandroid.core.detail;


import cc.lixiaoyu.wanandroid.base.mvp.BaseModel;
import cc.lixiaoyu.wanandroid.base.mvp.BasePresenter;
import cc.lixiaoyu.wanandroid.base.mvp.BaseView;
import cc.lixiaoyu.wanandroid.entity.Optional;
import io.reactivex.Observable;

public interface ArticleDetailContract {

    interface Model extends BaseModel {
        Observable<Optional<String>> collectArticle(int articleId);
        Observable<Optional<String>> unCollectArticle(int articleId);
    }

    interface View extends BaseView {
        void showCollectArticle(boolean success);
        void showUnCollectArticle(boolean success);
    }

    abstract class Presenter extends BasePresenter<ArticleDetailContract.View> {
        abstract void collectArticle(int articleId);

        abstract void unCollectArticle(int articleId);
    }
}
