package cc.lixiaoyu.wanandroid.core.home;

import java.util.List;

import cc.lixiaoyu.wanandroid.base.BaseModel;
import cc.lixiaoyu.wanandroid.base.BasePresenter;
import cc.lixiaoyu.wanandroid.base.BaseView;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import retrofit2.Call;

public interface HomeContract {
    interface Model extends BaseModel{
        Call<WanAndroidResult<ArticlePage>> getArticleList(int page);
        Call<WanAndroidResult<List<Banner>>> getBannerData();
    }

    interface View extends BaseView{

        /**
         * 显示文章列表
         * @param articles
         */
        void showArticleList(List<ArticlePage.Article> articles);

        /**
         * 显示Banner数据
         * @param banners
         */
        void showBannerData(List<Banner> banners, List<String> bannerTitleList);

        /**
         * 显示收藏文章
         * @param position
         * @param article
         */
        void showCollectArticle(int position, ArticlePage.Article article);

        /**
         * 显示取消收藏
         * @param position
         * @param article
         */
        void showCancelCollectArticle(int position, ArticlePage.Article article);

        /**
         * 显示加载更多文章
         * @param articles
         * @param success
         */
        void showLoadMore(List<ArticlePage.Article> articles, boolean success);

        /**
         * 显示打开文章详情
         * @param article
         */
        void showOpenArticleDetail(ArticlePage.Article article);

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
        public abstract void openArticleDetail(ArticlePage.Article article);

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
        public abstract void collectArticle(int position, ArticlePage.Article article);

        /**
         * 取消收藏文章
         * @param position
         * @param article
         */
        public abstract void cancelCollectArticle(int position, ArticlePage.Article article);
    }
}
