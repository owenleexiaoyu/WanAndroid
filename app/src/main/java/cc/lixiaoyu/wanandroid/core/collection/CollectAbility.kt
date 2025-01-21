package cc.lixiaoyu.wanandroid.core.collection

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import cc.lixiaoyu.wanandroid.core.account.AccountManager
import cc.lixiaoyu.wanandroid.core.account.ui.LoginActivity
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object CollectAbility {

    private val apiService = RetrofitManager.wanAndroidService
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    @SuppressLint("CheckResult")
    fun collectArticle(context: Context?, articleId: Int, onCollectResult: (success: Boolean) -> Unit) {
        if (!AccountManager.isLogin()) {
            context?.startActivity(Intent(context, LoginActivity::class.java))
            return
        }
        coroutineScope.launch {
            try {
                val response = apiService.collectArticle(articleId)
                if (response.isSuccess()) {
                    onCollectResult(true)
                } else {
                    onCollectResult(false)
                }
            } catch (e: Throwable) {
                onCollectResult(false)
            }
        }
    }

    @SuppressLint("CheckResult")
    fun unCollectArticle(context: Context?, articleId: Int, onCollectResult: (success: Boolean) -> Unit) {
        if (!AccountManager.isLogin()) {
            context?.startActivity(Intent(context, LoginActivity::class.java))
            return
        }
        coroutineScope.launch {
            try {
                val response = apiService.unCollectArticleFromArticleList(articleId)
                if (response.isSuccess()) {
                    onCollectResult(true)
                } else {
                    onCollectResult(false)
                }
            } catch (e: Throwable) {
                onCollectResult(false)
            }
        }
    }
}