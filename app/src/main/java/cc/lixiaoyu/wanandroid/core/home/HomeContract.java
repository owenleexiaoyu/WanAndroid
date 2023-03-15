package cc.lixiaoyu.wanandroid.core.home;


import java.util.List;

import cc.lixiaoyu.wanandroid.base.mvp.BaseModel;
import cc.lixiaoyu.wanandroid.base.mvp.BasePresenter;
import cc.lixiaoyu.wanandroid.base.mvp.BaseView;
import cc.lixiaoyu.wanandroid.entity.Article;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.entity.Optional;
import io.reactivex.Observable;

public interface HomeContract {
    interface Model extends BaseModel{
        Observable<Optional<ArticlePage>> getArticleList(int page);
        Observable<Optional<List<Banner>>> getBannerData();
        Observable<Optional<List<Article>>> getTopArticles();
        Observable<Optional<String>> collectArticle(int articleId);
        Observable<Optional<String>> unCollectArticle(int articleId);
    }

    interface View extends BaseView{

        /**
         * 显示文章列表
         * @param articles
         */
        void showArticleList(List<Article> articles);

        /**
         * 显示置顶文章
         * @param articles
         */
        void showTopArticles(List<Article> articles);


        /**
         * 显示Banner数据
         * @param banners
         */
        void showBannerData(List<Banner> banners, List<String> bannerTitleList);

        /**
         * 显示收藏文章
         * @param position
         * @param success
         */
        void showCollectArticle(boolean success, int position);

        /**
         * 显示取消收藏
         * @param position
         * @param success
         */
        void showCancelCollectArticle(boolean success, int position);

        /**
         * 显示加载更多文章
         * @param articles
         * @param success
         */
        void showLoadMore(List<Article> articles, boolean success);

        /**
         * 显示打开文章详情
         * @param article
         */
        void showOpenArticleDetail(Article article);

        /**
         * 显示打开Banner详情
         * @param banner
         */
        void showOpenBannerDetail(Banner banner);
    }

    abstract class Presenter extends BasePresenter<View>{

        /**
         * 获取文章列表
         */
        public abstract void getArticleList();

        /**
         * 获取置顶文章
         */
        public abstract void getTopArticles();

        /**
         * 加载更多文章
         */
        public abstract void loadMoreArticle();

        /**
         * 获取Banner的数据
         */
        public abstract void getBannerData();

        /**
         * 打开文章详情页
         * @param article
         */
        public abstract void openArticleDetail(Article article);

        /**
         * 打开Banner详情
         * @param banner
         */
        public abstract void openBannerDetail(Banner banner);

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
