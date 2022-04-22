package cc.lixiaoyu.wanandroid.util.network;

import java.util.concurrent.TimeUnit;

import cc.lixiaoyu.wanandroid.entity.WanAndroidResponse;
import cc.lixiaoyu.wanandroid.entity.Optional;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class BaseModelFactory {

    private static ResponseTransformer transformer = new ResponseTransformer();

    /**
     * 将Observable<WanAdnroidResult<T>>转化Observable<T>,并处理BaseResponse
     *
     * @param observable
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Observable<Optional<T>> compose(Observable<WanAndroidResponse<T>> observable) {
        return observable.compose(transformer);
    }

    /**
     * 自定义Transformer
     *
     * @param <T>
     */
    private static class ResponseTransformer<T> implements
            ObservableTransformer<WanAndroidResponse<T>, Optional<T>> {

        @Override
        public ObservableSource<Optional<T>> apply(Observable<WanAndroidResponse<T>> upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .timeout(5, TimeUnit.SECONDS)
                    .retry(5)
                    .flatMap(new Function<WanAndroidResponse<T>, ObservableSource<Optional<T>>>() {
                        @Override
                        public ObservableSource<Optional<T>> apply(WanAndroidResponse<T> result) throws Exception {
                            if (result.isSuccess()) {
                                return createHttpData(result.transform());
                            } else {
                                return Observable.error(new APIException(result.getErrorCode(),
                                        result.getErrorMsg()));
                            }
                        }
                    });
        }

        public static <T> Observable<Optional<T>> createHttpData(final Optional<T> t) {

            return Observable.create(new ObservableOnSubscribe<Optional<T>>() {
                @Override
                public void subscribe(ObservableEmitter<Optional<T>> e) throws Exception {
                    try {
                        e.onNext(t);
                        e.onComplete();
                    } catch (Exception exc) {
                        e.onError(exc);
                    }
                }
            });
        }

        /**
         * 处理请求结果,BaseResponse
         *
         * @param result 请求结果
         * @return 过滤处理, 返回只有data数据的Observable
         */
        private Observable<T> flatResponse(final WanAndroidResponse<T> result) {
            return Observable.create(new ObservableOnSubscribe<T>() {
                @Override
                public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                    //成功
                    if (result.isSuccess()) {
                        if (!emitter.isDisposed()) {
                            emitter.onNext(result.getData());
                        }
                    }
                    //失败
                    else {
                        if (!emitter.isDisposed()) {
                            emitter.onError(new APIException(result.getErrorCode(), result.getErrorMsg()));
                        }
                        return;
                    }
                    if (!emitter.isDisposed()) {
                        emitter.onComplete();
                    }
                }
            });
        }
    }
}
