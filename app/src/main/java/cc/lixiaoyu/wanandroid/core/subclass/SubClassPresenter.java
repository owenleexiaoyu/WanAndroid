package cc.lixiaoyu.wanandroid.core.subclass;

import cc.lixiaoyu.wanandroid.entity.ArticlePage;
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
        mModel.getArticleListByCid(mCurrentPage, cid).subscribe(new Consumer<ArticlePage>() {
            @Override
            public void accept(ArticlePage articlePage) throws Exception {
                getView().showArticleListByCid(articlePage.getArticleList());
            }
        });
    }

    @Override
    public void loadMoreArticleByCid(String cid) {
        mCurrentPage++;
        mModel.getArticleListByCid(mCurrentPage, cid).subscribe(new Consumer<ArticlePage>() {
            @Override
            public void accept(ArticlePage articlePage) throws Exception {
                getView().showLoadMoreArticleByCid(articlePage.getArticleList(),true);
            }
        });
    }

    @Override
    public void openArticleDetail(ArticlePage.Article article) {
        getView().showOpenArticleDetail(article);
    }

    @Override
    public void collectArticle(ArticlePage.Article article) {

    }

    @Override
    public void cancelCollectArticle(ArticlePage.Article article) {

    }

    @Override
    public void start() {

    }
}
