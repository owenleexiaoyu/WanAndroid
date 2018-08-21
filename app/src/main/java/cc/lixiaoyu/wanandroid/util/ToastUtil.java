package cc.lixiaoyu.wanandroid.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.app.MyApplication;

public class ToastUtil {
    /**
     * 显示自定义Toast，短暂显示
     * @param text
     */
    public static void showToast(String text){
        show(text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示自定义Toast，长时间显示
     * @param text
     */
    public static void showToastLong(String text){
        show(text, Toast.LENGTH_LONG);
    }

    private static void show(String text, int showLength) {
        Context context = MyApplication.getGlobalContext();
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
        TextView tvToast = view.findViewById(R.id.toast_text);
        tvToast.setText(text);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(showLength);
        toast.setView(view);
        toast.show();
    }
}
