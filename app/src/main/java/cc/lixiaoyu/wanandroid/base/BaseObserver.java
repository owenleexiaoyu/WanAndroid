package cc.lixiaoyu.wanandroid.base;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cc.lixiaoyu.wanandroid.util.APIException;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * 封装的观察者基类
 */
public abstract class BaseObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        if(t != null){
            call(t);
        }else{
            ToastUtil.showToast("连接失败");
        }
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof APIException) {
            APIException exception = (APIException) e;
            ToastUtil.showToast(exception.getErrorMsg());
        } else if (e instanceof UnknownHostException) {
            ToastUtil.showToast("请打开网络");
        } else if (e instanceof SocketTimeoutException) {
            ToastUtil.showToast( "请求超时");
        } else if (e instanceof ConnectException) {
            ToastUtil.showToast("连接失败");
        } else if (e instanceof HttpException) {
            ToastUtil.showToast("请求超时");
        }else {
            ToastUtil.showToast("请求失败");
        }
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }

    public abstract void call(T t);
}
