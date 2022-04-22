package cc.lixiaoyu.wanandroid.core.profile

import cc.lixiaoyu.wanandroid.entity.Integral
import cc.lixiaoyu.wanandroid.entity.IntegralRecord
import cc.lixiaoyu.wanandroid.entity.PageData
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager


/**
 * 获取我的积分的 Repository
 */
class MyIntegralRepository {

    suspend fun getMyIntegralInfo(): Integral = RetrofitManager.getInstance().wanAndroidService.getMyIntegralInfo().data

    suspend fun getMyIntegralRecord(page: Int): PageData<IntegralRecord> = RetrofitManager.getInstance().wanAndroidService.getMyIntegralRecord(page).data
}