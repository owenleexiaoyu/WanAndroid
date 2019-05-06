package cc.lixiaoyu.wanandroid.core.search;

import java.util.List;

import cc.lixiaoyu.wanandroid.base.BaseModel;
import cc.lixiaoyu.wanandroid.base.BasePresenter;
import cc.lixiaoyu.wanandroid.base.BaseView;
import cc.lixiaoyu.wanandroid.entity.HotKey;
import cc.lixiaoyu.wanandroid.entity.WebSite;
import cc.lixiaoyu.wanandroid.util.Optional;
import io.reactivex.Observable;

public interface SearchContract {

    interface Model extends BaseModel{
        /**
         * 获取热搜词汇
         * @return
         */
        Observable<Optional<List<HotKey>>> getHotKey();

        /**
         * 获取常用网站
         * @return
         */
        Observable<Optional<List<WebSite>>> getCommonSite();
    }

    interface View extends BaseView{
        /**
         * 显示历史搜索的词汇
         * @param histroyWords
         */
        void showSearchHistory(List<String> histroyWords);

        /**
         * 显示热搜词汇
         * @param hotKeys
         */
        void showHotkey(List<HotKey> hotKeys);

        /**
         * 显示常用网站
         * @param webSites
         */
        void showCommonSite(List<WebSite> webSites);

        /**
         * 显示搜索结果
         * @param keyword
         */
        void showSearchResult(String keyword);
    }

    abstract class Presenter extends BasePresenter<View>{
        public abstract void getHotkey();
        public abstract void getCommonSite();
        public abstract void getHistroyWords();

        /**
         * 通过关键词搜索
         * @param keyword
         */
        public abstract void searchArticle(String keyword);

        /**
         * 清空历史记录
         */
        public abstract void clearHistory();
    }
}
