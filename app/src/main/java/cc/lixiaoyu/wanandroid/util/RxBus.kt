package cc.lixiaoyu.wanandroid.util

import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

/**
 * RxBus 的工具类，实现事件总线
 */
class RxBus private constructor() {

    private val bus: FlowableProcessor<Any> = PublishProcessor.create<Any>().toSerialized()

    /**
     * 发送消息
     *
     * @param o
     */
    fun post(o: Any) {
        bus.onNext(o)
    }

    /**
     * 确定接受消息的类型
     *
     * @param tClass
     * @param <T>
     * @return
    </T> */
    fun <T> toFlowable(tClass: Class<T>?): Flowable<T> {
        return bus.ofType(tClass)
    }

    /**
     * 判断是否有订阅者
     *
     * @return
     */
    fun hasSubscribers(): Boolean {
        return bus.hasSubscribers()
    }

    companion object {
        @Volatile
        private var rxBus: RxBus? = null
        val instance: RxBus
            get() {
                if (rxBus == null) {
                    synchronized(RxBus::class.java) {
                        if (rxBus == null) {
                            rxBus = RxBus()
                        }
                    }
                }
                return rxBus!!
            }
    }
}