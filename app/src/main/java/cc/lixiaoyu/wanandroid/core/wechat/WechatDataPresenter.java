package cc.lixiaoyu.wanandroid.core.wechat;

import cc.lixiaoyu.wanandroid.entity.WechatPage;
import cc.lixiaoyu.wanandroid.util.Optional;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import io.reactivex.functions.Consumer;

public class WechatDataPresenter extends WechatDataContract.Presenter {

    private WechatDataContract.Model mModel;

    //加载的当前页,默认为1
    private int mCurrentPage = 1;

    public WechatDataPresenter() {
        mModel = new WechatDataModel();
    }

    @Override
    public void openArticleDetail(WechatPage.WechatData article) {
        getView().showOpenArticleDetail(article);
    }

    @Override
    public void collectArticle(final int position, WechatPage.WechatData article) {
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
    public void cancelCollectArticle(final int position, WechatPage.WechatData article) {
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
    public void getWechatArticlesById(int id) {
        mCurrentPage = 1;
        mModel.getWechatArticlesById(mCurrentPage, id)
                .subscribe(new Consumer<Optional<WechatPage>>() {
                    @Override
                    public void accept(Optional<WechatPage> wechatPageOptional) throws Exception {
                        getView().showWechatArticlesById(wechatPageOptional.getIncludeNull().getDataList());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable t) throws Exception {
                        ToastUtil.showToast("出错了");
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void loadMoreWechatArticlesById(int id) {
        mCurrentPage++;
        mModel.getWechatArticlesById(mCurrentPage, id)
                .subscribe(new Consumer<Optional<WechatPage>>() {
                    @Override
                    public void accept(Optional<WechatPage> wechatPageOptional) throws Exception {
                        getView().showLoadMoreWechatArticleById(wechatPageOptional.getIncludeNull().getDataList(),true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable t) throws Exception {
                        getView().showLoadMoreWechatArticleById(null,false);
                    }
                });
    }

    @Override
    public void start() {

    }
}
