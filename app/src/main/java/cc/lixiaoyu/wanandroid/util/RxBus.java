package cc.lixiaoyu.wanandroid.util;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * RxBus 的工具类，实现事件总线
 */
public class RxBus {
    private static volatile RxBus rxBus = null;
    private final FlowableProcessor<Object> mBus;

    private RxBus(){
        mBus = PublishProcessor.create().toSerialized();
    }

    public static RxBus getInstance(){
        if(rxBus == null){
            synchronized (RxBus.class){
                if (rxBus == null){
                    rxBus = new RxBus();
                }
            }
        }
        return rxBus;
    }

    /**
     * 发送消息
     * @param o
     */
    public void post(Object o){
        mBus.onNext(o);
    }

    /**
     * 确定接受消息的类型
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> Flowable<T> toFlowable(Class<T> tClass){
        return mBus.ofType(tClass);
    }

    /**
     * 判断是否有订阅者
     * @return
     */
    public boolean hasSubscribers(){
        return mBus.hasSubscribers();
    }
}
