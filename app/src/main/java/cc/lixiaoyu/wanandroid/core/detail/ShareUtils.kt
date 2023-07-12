package cc.lixiaoyu.wanandroid.core.detail

import android.app.Activity
import android.content.Intent
import android.net.Uri

class ShareUtils {

    companion object {
        fun systemShare(context: Activity, shareText: String) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, shareText)
            intent.type = "text/plain"
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                // ignore
            }
        }

        fun openInBrowser(context: Activity, url: String) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                // ignore
            }
        }
    }
}