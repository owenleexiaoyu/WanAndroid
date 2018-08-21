package cc.lixiaoyu.wanandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

import cc.lixiaoyu.wanandroid.app.MyApplication;

/**
 * SharedPreperence的工具类
 */
public class SpUtil {
    private static Context context = MyApplication.getGlobalContext();
    public static final String FILE_DEFAULT = "WanAndroid";

    public static void saveData(String key, Object data) {

        SharedPreferences sharedPreferences = context
                .getSharedPreferences(FILE_DEFAULT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        }

        editor.commit();
    }

    public static Object getData(String key, Object defaultObject) {


        SharedPreferences sharedPreferences = context.getSharedPreferences
                (FILE_DEFAULT, Context.MODE_PRIVATE);

        //defValue为为默认值，如果当前获取不到数据就返回它
        if (defaultObject instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof String) {
            return sharedPreferences.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sharedPreferences.getLong(key, (Long) defaultObject);
        }

        return null;
    }
}
