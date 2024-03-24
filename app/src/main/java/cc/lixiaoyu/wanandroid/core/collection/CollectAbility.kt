package cc.lixiaoyu.wanandroid.core.collection

import android.annotation.SuppressLint
import android.content.Context
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object CollectAbility {

    private val apiService = RetrofitManager.wanAndroidService

    @SuppressLint("CheckResult")
    fun collectArticle(context: Context?, articleId: Int, onCollectResult: (success: Boolean) -> Unit) {
//        if (!AccountManager.isLogin()) {
//            context.startActivity(Intent(context, LoginActivity::class.java))
//            return
//        }

        apiService.collectArticle(articleId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccess()) {
                    onCollectResult(true)
                } else {
                    onCollectResult(false)
                }
            }, {
                onCollectResult(false)
            })
    }

    @SuppressLint("CheckResult")
    fun unCollectArticle(context: Context?, articleId: Int, onCollectResult: (success: Boolean) -> Unit) {
//        if (!AccountManager.isLogin()) {
//            context.startActivity(Intent(context, LoginActivity::class.java))
//            return
//        }
        apiService.unCollectArticleFromArticleList(articleId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccess()) {
                    onCollectResult(true)
                } else {
                    onCollectResult(false)
                }
            }, {
                onCollectResult(false)
            })
    }
}