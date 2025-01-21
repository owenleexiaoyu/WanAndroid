package cc.lixiaoyu.wanandroid.util.storage

import android.annotation.SuppressLint
import android.content.Context
import cc.lixiaoyu.wanandroid.app.WanApplication.Companion.globalContext
import java.lang.IllegalArgumentException

/**
 * SharedPreperence的工具类
 */
@SuppressLint("StaticFieldLeak")
object SPUtil {
    private val context = globalContext
    private const val FILE_DEFAULT = "WanAndroid"
    
    @JvmStatic
    fun saveData(key: String?, data: Any) {
        val sharedPreferences = context
            ?.getSharedPreferences(FILE_DEFAULT, Context.MODE_PRIVATE) ?: return
        val editor = sharedPreferences.edit()
        when (data) {
            is Int -> {
                editor.putInt(key, data)
            }
            is Boolean -> {
                editor.putBoolean(key, data)
            }
            is String -> {
                editor.putString(key, data)
            }
            is Float -> {
                editor.putFloat(key, data)
            }
            is Long -> {
                editor.putLong(key, data)
            }
            else -> {
                throw IllegalArgumentException("SpUtil doesn't support this type")
            }
        }
        editor.apply()
    }

    @JvmStatic
    fun getData(key: String?, defaultObject: Any): Any? {
        val sharedPreferences = context?.getSharedPreferences(FILE_DEFAULT, Context.MODE_PRIVATE) ?: return null

        //defValue为为默认值，如果当前获取不到数据就返回它
        when (defaultObject) {
            is Int -> {
                return sharedPreferences.getInt(key, defaultObject)
            }
            is Boolean -> {
                return sharedPreferences.getBoolean(key, defaultObject)
            }
            is String -> {
                return sharedPreferences.getString(key, defaultObject)
            }
            is Float -> {
                return sharedPreferences.getFloat(key, defaultObject)
            }
            is Long -> {
                return sharedPreferences.getLong(key, defaultObject)
            }
            else -> {
                throw IllegalArgumentException("SpUtil doesn't support this type")
            }
        }
    }
}