package cc.lixiaoyu.wanandroid.kmp

import android.widget.Toast

actual fun showShortToast(message: String) {
    val ctx = AndroidToastContext.getOrNull() ?: return
    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
}
