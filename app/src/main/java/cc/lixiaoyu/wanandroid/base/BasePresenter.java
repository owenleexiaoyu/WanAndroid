package cc.lixiaoyu.wanandroid.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import cc.lixiaoyu.wanandroid.util.DataManager;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<V extends BaseView> {

    protected Reference<V> mViewRef;//View 接口类型的弱引用
    protected CompositeDisposable mCompositeDisposable;
    protected DataManager mDataManager = new DataManager();  //数据管理者

    public void attachView(V view){
        mViewRef = new WeakReference<>(view);
    }

    protected V getView(){
        return mViewRef.get();
    }

    public boolean isViewAttached(){
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView(){
        if(mViewRef != null){
            mViewRef.clear();
            mViewRef = null;
        }
        if(mCompositeDisposable != null){
            mCompositeDisposable.clear();
        }
    }

    protected void addSubscriber(Disposable disposable){
        if(mCompositeDisposable == null){
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public abstract  void start();


    public DataManager getDataManager(){
        return mDataManager;
    }
}
