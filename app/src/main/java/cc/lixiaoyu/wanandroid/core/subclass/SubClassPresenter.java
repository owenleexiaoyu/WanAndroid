package cc.lixiaoyu.wanandroid.core.subclass;

import cc.lixiaoyu.wanandroid.entity.Article;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.util.Optional;
import io.reactivex.functions.Consumer;

public class SubClassPresenter extends SubClassContract.Presenter {

    private SubClassContract.Model mModel;

    private int mCurrentPage = 0;

    public SubClassPresenter(){
        mModel = new SubClassModel();
    }

    @Override
    public void getArticleListByCid(String cid) {
        mCurrentPage = 0;
        mModel.getArticleListByCid(mCurrentPage, cid).subscribe(new Consumer<Optional<ArticlePage>>() {
            @Override
            public void accept(Optional<ArticlePage> articlePage) throws Exception {
                getView().showArticleListByCid(articlePage.getIncludeNull().getArticleList());
            }
        });
    }

    @Override
    public void loadMoreArticleByCid(String cid) {
        mCurrentPage++;
        mModel.getArticleListByCid(mCurrentPage, cid).subscribe(new Consumer<Optional<ArticlePage>>() {
            @Override
            public void accept(Optional<ArticlePage> articlePage) throws Exception {
                getView().showLoadMoreArticleByCid(articlePage.getIncludeNull().getArticleList(),true);
            }
        });
    }

    @Override
    public void openArticleDetail(Article article) {
        getView().showOpenArticleDetail(article);
    }

    @Override
    public void collectArticle(final int position, Article article) {
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
    public void cancelCollectArticle(final int position, Article article) {
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
