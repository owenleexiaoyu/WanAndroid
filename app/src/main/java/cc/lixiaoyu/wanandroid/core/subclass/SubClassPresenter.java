package cc.lixiaoyu.wanandroid.core.subclass;

import java.util.List;

import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubClassPresenter extends SubClassContract.Presenter {

    private SubClassContract.Model mModel;

    private int mCurrentPage = 0;

    public SubClassPresenter(){
        mModel = new SubClassModel();
    }

    @Override
    public void getArticleListByCid(String cid) {
        mCurrentPage = 0;
        Call<WanAndroidResult<ArticlePage>> call = mModel.getArticleListByCid(mCurrentPage, cid);
        call.enqueue(new Callback<WanAndroidResult<ArticlePage>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<ArticlePage>> call,
                                   Response<WanAndroidResult<ArticlePage>> response) {
                WanAndroidResult<ArticlePage> result = response.body();
                if(result.getErrorCode() == 0){
                    getView().showArticleListByCid(result.getData().getArticleList());
                }
            }

            @Override
            public void onFailure(Call<WanAndroidResult<ArticlePage>> call, Throwable t) {
                ToastUtil.showToast("出错了");
            }
        });
    }

    @Override
    public void loadMoreArticleByCid(String cid) {
        mCurrentPage++;
        Call<WanAndroidResult<ArticlePage>> call = mModel.getArticleListByCid(mCurrentPage, cid);
        call.enqueue(new Callback<WanAndroidResult<ArticlePage>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<ArticlePage>> call,
                                   Response<WanAndroidResult<ArticlePage>> response) {
                WanAndroidResult<ArticlePage> result = response.body();
                List<ArticlePage.Article> newList = result.getData().getArticleList();
                getView().showLoadMoreArticleByCid(newList,true);
            }

            @Override
            public void onFailure(Call<WanAndroidResult<ArticlePage>> call, Throwable t) {
                getView().showLoadMoreArticleByCid(null, false);
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
