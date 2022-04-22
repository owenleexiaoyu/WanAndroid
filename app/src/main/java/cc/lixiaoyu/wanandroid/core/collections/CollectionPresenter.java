package cc.lixiaoyu.wanandroid.core.collections;

import cc.lixiaoyu.wanandroid.entity.Article;
import cc.lixiaoyu.wanandroid.entity.CollectionPage;
import cc.lixiaoyu.wanandroid.entity.Optional;
import io.reactivex.functions.Consumer;

public class CollectionPresenter extends CollectionContract.Presenter {

    private CollectionContract.Model mModel;
    //当前的页数，从0开始
    private int mCurrentPage = 0;

    public CollectionPresenter(){
        mModel = new CollectionModel();
    }

    @Override
    public void getCollectionArticleList() {
        //将currentPage重新置为0
        mCurrentPage = 0;
        mModel.getCollectionArticleList(mCurrentPage)
                .subscribe(new Consumer<Optional<CollectionPage>>() {
                    @Override
                    public void accept(Optional<CollectionPage> collectionPage) throws Exception {
                        getView().showCollectionArticleList(collectionPage.getIncludeNull().getDatas());
                    }
                });
    }

    @Override
    public void loadMoreCollectionArticle() {
        mCurrentPage++;
        mModel.getCollectionArticleList(mCurrentPage)
                .subscribe(new Consumer<Optional<CollectionPage>>() {
                    @Override
                    public void accept(Optional<CollectionPage> collectionPage) throws Exception {
                        getView().showLoadMoreCollectionArticle(true, collectionPage.getIncludeNull().getDatas());
                    }
                });
    }



    @Override
    public void unCollectArticle(final int position, int articleId) {
        mModel.unCollectArticle(articleId).subscribe(new Consumer<Optional<String>>() {
            @Override
            public void accept(Optional<String> s) throws Exception {
                getView().showUnCollectArticle(true, position);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //打印异常
                throwable.printStackTrace();
                getView().showUnCollectArticle(false, position);
            }
        });
    }

    @Override
    public void openArticleDetail(Article article) {
        getView().showOpenArticleDetail(article);
    }

    @Override
    public void start() {
        getCollectionArticleList();
    }
}
