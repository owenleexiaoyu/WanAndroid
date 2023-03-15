package cc.lixiaoyu.wanandroid.core.search;

import java.util.List;

import cc.lixiaoyu.wanandroid.entity.HotKey;
import cc.lixiaoyu.wanandroid.util.storage.DataManager;
import cc.lixiaoyu.wanandroid.entity.Optional;
import io.reactivex.functions.Consumer;

public class SearchPresenter extends SearchContract.Presenter{

    private SearchContract.Model mModel;
    private DataManager mDataManager;
    public SearchPresenter(){
        mModel = new SearchModel();
        mDataManager = new DataManager();
    }

    @Override
    public void getHotkey() {
        mModel.getHotKey().subscribe(new Consumer<Optional<List<HotKey>>>() {
            @Override
            public void accept(Optional<List<HotKey>> hotKeys) throws Exception {
                getView().showHotkey(hotKeys.getIncludeNull());
            }
        });
    }

    @Override
    public void getCommonSite() {
        mModel.getCommonSite().subscribe(new Consumer<Optional<List<WebSite>>>() {
            @Override
            public void accept(Optional<List<WebSite>> webSites) throws Exception {
                getView().showCommonSite(webSites.getIncludeNull());
            }
        });
    }

    @Override
    public void getHistroyWords() {
        List<String> historyList = mDataManager.loadAllHistoryData();
        getView().showSearchHistory(historyList);
    }

    @Override
    public void searchArticle(String keyword) {
        //将搜索的关键词添加到数据库中
        mDataManager.addHistoryData(keyword);
        getHistroyWords();
        getView().showSearchResult(keyword);
    }

    @Override
    public void clearHistory() {
        mDataManager.clearAllHistoryData();
        getHistroyWords();
    }

    @Override
    public void start() {
        getHistroyWords();
        getHotkey();
        getCommonSite();
    }
}
