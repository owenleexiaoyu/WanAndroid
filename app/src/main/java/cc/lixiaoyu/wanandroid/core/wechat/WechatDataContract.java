package cc.lixiaoyu.wanandroid.core.wechat;

import java.util.List;

import cc.lixiaoyu.wanandroid.base.mvp.BaseModel;
import cc.lixiaoyu.wanandroid.base.mvp.BasePresenter;
import cc.lixiaoyu.wanandroid.base.mvp.BaseView;
import cc.lixiaoyu.wanandroid.entity.Article;
import cc.lixiaoyu.wanandroid.entity.WechatPage;
import cc.lixiaoyu.wanandroid.entity.Optional;
import io.reactivex.Observable;

public interface WechatDataContract {

    interface Model extends BaseModel{
        Observable<Optional<WechatPage>> getWechatArticlesById(int id, int page);
        Observable<Optional<String>> collectArticle(int articleId);
        Observable<Optional<String>> unCollectArticle(int articleId);
    }

    interface View extends BaseView{
        void showWechatArticlesById(List<Article> dataList);
        void showOpenArticleDetail(Article data);
        void showCollectArticle(boolean success, int position);
        void showCancelCollectArticle(boolean success, int position);
        void showLoadMoreWechatArticleById(List<Article> dataList, boolean success);
    }

    abstract class Presenter extends BasePresenter<View>{
        /**
         * 获取公众号下的所有文章，分页加载
         * @param id  公众号的id
         */
        public abstract void getWechatArticlesById(int id);

        /**
         * 加载公众号分类下更多的文章
         * @param id
         */
        public abstract void loadMoreWechatArticlesById(int id);

        /**
         * 打开文章详情页
         * @param article
         */
        public abstract void openArticleDetail(Article article);

        /**
         * 收藏文章
         * @param position
         * @param article
         */
        public abstract void collectArticle(int position, Article article);

        /**
         * 取消收藏文章
         * @param position
         * @param article
         */
        public abstract void cancelCollectArticle(int position, Article article);

    }
}
