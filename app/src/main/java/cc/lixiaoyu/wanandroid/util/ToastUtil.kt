package cc.lixiaoyu.wanandroid.util

import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.app.WanApplication.Companion.globalContext

object ToastUtil {
    /**
     * 显示自定义Toast，短暂显示
     * @param text
     */
    @JvmStatic
    fun showToast(text: String) {
        show(text, Toast.LENGTH_SHORT)
    }

    /**
     * 显示自定义Toast，长时间显示
     * @param text
     */
    fun showToastLong(text: String) {
        show(text, Toast.LENGTH_LONG)
    }

    private fun show(text: String, showLength: Int) {
        val context = globalContext
        val toast = Toast(context)
        val view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null)
        val tvToast = view.findViewById<TextView>(R.id.toast_text)
        tvToast.text = text
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.duration = showLength
        toast.view = view
        toast.show()
    }
}