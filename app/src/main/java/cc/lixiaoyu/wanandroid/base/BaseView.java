package cc.lixiaoyu.wanandroid.base;

public interface BaseView<T> {
    void setPresenter(T presenter);

    void onStart();

    void onStop();
}
