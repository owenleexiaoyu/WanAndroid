package cc.lixiaoyu.wanandroid.mvp.presenter;

import java.util.ArrayList;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.mvp.contract.SubClassContract;
import cc.lixiaoyu.wanandroid.util.RetrofitUtil;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubClassPresenter implements SubClassContract.Presenter {

    private SubClassContract.View mView;
    private SubClassContract.Model mModel;

    private int mCurrentPage;

    public SubClassPresenter(SubClassContract.View mView,
                             SubClassContract.Model mModel){
        this.mModel = mModel;
        this.mView = mView;
        this.mView.setPresenter(this);
    }

    @Override
    public void getArticleListByCid(String cid) {
        Call<WanAndroidResult<ArticlePage>> call = mModel.getArticleListByCid(mCurrentPage, cid);
        call.enqueue(new Callback<WanAndroidResult<ArticlePage>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<ArticlePage>> call,
                                   Response<WanAndroidResult<ArticlePage>> response) {
                WanAndroidResult<ArticlePage> result = response.body();
                if(result.getErrorCode() == 0){
                    mView.showArticleListByCid(result.getData().getArticleList());
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

    }

    @Override
    public void openArticleDetail(ArticlePage.Article article) {
        mView.showOpenArticleDetail(article);
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
