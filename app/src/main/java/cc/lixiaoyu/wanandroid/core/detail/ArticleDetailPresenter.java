package cc.lixiaoyu.wanandroid.core.detail;

import android.app.Activity;

public class ArticleDetailPresenter extends ArticleDetailContract.Presenter {

    private ArticleDetailContract.Model mModel = new ArticleDetailModel();

    @Override
    void collectArticle(int articleId) {
        mModel.collectArticle(articleId).subscribe(
                s -> {
                    getView().showCollectArticle(true);
                },
                throwable -> {
                    //打印异常
                    throwable.printStackTrace();
                    getView().showCollectArticle(false);
                });
    }

    @Override
    void unCollectArticle(int articleId) {
        mModel.unCollectArticle(articleId).subscribe(
                s -> {
                    getView().showUnCollectArticle(true);
                },
                throwable -> {
                    //打印异常
                    throwable.printStackTrace();
                    getView().showUnCollectArticle(false);
                });
    }

    @Override
    public void start() {

    }
}
