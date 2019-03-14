package cc.lixiaoyu.wanandroid.util;

import java.util.concurrent.TimeUnit;

import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
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
     * @param observable
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Observable<T> compose(Observable<WanAndroidResult<T>> observable){
        return observable.compose(transformer);
    }

    /**
     * 自定义Transformer
     * @param <T>
     */
    private static class ResponseTransformer<T> implements
            ObservableTransformer<WanAndroidResult<T>, T> {

        @Override
        public ObservableSource<T> apply(Observable<WanAndroidResult<T>> upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .timeout(5, TimeUnit.SECONDS)
                    .retry(5)
                    .map(new Function<WanAndroidResult<T>, T>() {
                        @Override
                        public T apply(WanAndroidResult<T> result) throws Exception {
                            return result.getData();
                        }
                    });
        }
    }
}
