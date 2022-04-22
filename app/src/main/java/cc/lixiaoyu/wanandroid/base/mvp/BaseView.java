package cc.lixiaoyu.wanandroid.base.mvp;

/**
 * MVP 架构中的 View
 */
public interface BaseView{
    /**
     * 展示加载进度
     */
    void showLoading();


    /**
     * 隐藏加载进度
     */
    void hideLoading();
}
