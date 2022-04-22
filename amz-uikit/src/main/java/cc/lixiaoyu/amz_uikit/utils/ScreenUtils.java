package cc.lixiaoyu.amz_uikit.utils;

import android.content.Context;
import android.content.res.TypedArray;

public class ScreenUtils {

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp -> px
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * Get height size of system ActionBar
     * <p/>
     * @param context
     * @return
     */
    public static int getActionBarHeight(Context context) {
        TypedArray ta = context.obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int actionBarSize = (int) ta.getDimension(0,0);
        ta.recycle();
        return actionBarSize;
    }

}
