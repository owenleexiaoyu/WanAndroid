package cc.lixiaoyu.wanandroid.core.collection

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import cc.lixiaoyu.wanandroid.core.account.AccountManager
import cc.lixiaoyu.wanandroid.core.account.ui.LoginActivity
import cc.lixiaoyu.wanandroid.util.network.BaseModelFactory
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object CollectAbility {

    private val apiService = RetrofitManager.getInstance().wanAndroidService

    @SuppressLint("CheckResult")
    fun collectArticle(context: Context, articleId: Int, onCollectResult: (success: Boolean) -> Unit) {
//        if (!AccountManager.isLogin()) {
//            context.startActivity(Intent(context, LoginActivity::class.java))
//            return
//        }
        apiService.collectArticle(articleId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccess) {
                    onCollectResult(true)
                } else {
                    onCollectResult(false)
                }
            }, {
                onCollectResult(false)
            })
    }

    @SuppressLint("CheckResult")
    fun unCollectArticle(context: Context, articleId: Int, onCollectResult: (success: Boolean) -> Unit) {
//        if (!AccountManager.isLogin()) {
//            context.startActivity(Intent(context, LoginActivity::class.java))
//            return
//        }
        apiService.unCollectArticleFromArticleList(articleId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccess) {
                    onCollectResult(true)
                } else {
                    onCollectResult(false)
                }
            }, {
                onCollectResult(false)
            })
    }
}