package cc.lixiaoyu.amz_uikit.titlebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


import cc.lixiaoyu.amz_uikit.R;
import cc.lixiaoyu.amz_uikit.utils.ScreenUtils;

public class AMZTitleBar extends FrameLayout implements View.OnClickListener {

    private LinearLayout mMainLayout;
    private TextView mStartView;
    private TextView mTitleView;
    private TextView mEndView;
    private View mLineView;

    private int mStartIconSize = -1;
    private int mEndIconSize = -1;
    private int mStartIconPadding = 0;
    private int mEndIconPadding = 0;

    private OnAMZTitleBarListener mListener;


    public AMZTitleBar(@NonNull Context context) {
        this(context, null, 0);
    }

    public AMZTitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AMZTitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initStyle(attrs);
    }

    private void initView(@NonNull Context context) {
        final Builder builder = new Builder(context);
        mMainLayout = builder.getMainLayout();
        mStartView = builder.getStartView();
        mTitleView = builder.getTitleView();
        mEndView = builder.getEndView();
        mLineView = builder.getLineView();

        mMainLayout.addView(mStartView);
        mMainLayout.addView(mTitleView);
        mMainLayout.addView(mEndView);

        addView(mMainLayout, 0);
        addView(mLineView, 1);
    }

    private void initStyle(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.AMZTitleBar);
        if (ta.hasValue(R.styleable.AMZTitleBar_start_title)) {
            setTitle(mStartView, ta.getString(R.styleable.AMZTitleBar_start_title));
        }

        if (ta.hasValue(R.styleable.AMZTitleBar_title)) {
            setTitle(mTitleView, ta.getString(R.styleable.AMZTitleBar_title));
        }

        if (ta.hasValue(R.styleable.AMZTitleBar_end_title)) {
            setTitle(mEndView, ta.getString(R.styleable.AMZTitleBar_end_title));
        }

        if (ta.hasValue(R.styleable.AMZTitleBar_start_icon_size)) {
            mStartIconSize = ta.getDimensionPixelSize(R.styleable.AMZTitleBar_start_icon_size, -1);
        }
        if (ta.hasValue(R.styleable.AMZTitleBar_end_icon_size)) {
            mEndIconSize = ta.getDimensionPixelSize(R.styleable.AMZTitleBar_end_icon_size, -1);
        }

        if (ta.hasValue(R.styleable.AMZTitleBar_start_icon)) {
            setStartIcon(ContextCompat.getDrawable(getContext(), ta.getResourceId(R.styleable.AMZTitleBar_start_icon, 0)));
        }
        if (ta.hasValue(R.styleable.AMZTitleBar_end_icon)) {
            setEndIcon(ContextCompat.getDrawable(getContext(), ta.getResourceId(R.styleable.AMZTitleBar_end_icon, 0)));
        }

        if (ta.hasValue(R.styleable.AMZTitleBar_start_icon_padding)) {
            setStartIconPadding(ta.getDimensionPixelSize(R.styleable.AMZTitleBar_start_icon_padding, 0));
        }
        if (ta.hasValue(R.styleable.AMZTitleBar_end_icon_padding)) {
            setEndIconPadding(ta.getDimensionPixelSize(R.styleable.AMZTitleBar_end_icon_padding, 0));
        }

        // 文字颜色设置
        mStartView.setTextColor(ta.getColor(R.styleable.AMZTitleBar_start_title_color, 0xFF666666));
        mTitleView.setTextColor(ta.getColor(R.styleable.AMZTitleBar_title_color, 0xFF222222));
        mEndView.setTextColor(ta.getColor(R.styleable.AMZTitleBar_end_title_color, 0xFFA4A4A4));

        //文字大小设置
        mStartView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(R.styleable.AMZTitleBar_start_title_size, ScreenUtils.sp2px(getContext(), 14)));
        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(R.styleable.AMZTitleBar_title_size, ScreenUtils.sp2px(getContext(), 16)));
        mEndView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(R.styleable.AMZTitleBar_end_title_size, ScreenUtils.sp2px(getContext(), 14)));

        //背景设置
        mStartView.setBackgroundResource(ta.getResourceId(R.styleable.AMZTitleBar_start_background, 0));
        mEndView.setBackgroundResource(ta.getResourceId(R.styleable.AMZTitleBar_end_background, 0));

        //分割线设置
        mLineView.setVisibility(ta.getBoolean(R.styleable.AMZTitleBar_show_bottom_line, true) ? View.VISIBLE : View.GONE);
        mLineView.setBackgroundColor(ta.getColor(R.styleable.AMZTitleBar_bottom_line_color, 0xFFECECEC));

        //回收TypedArray
        ta.recycle();

        //设置默认背景
        if (getBackground() == null) {
            setBackgroundColor(0xFFFFFFFF);
        }
    }

    private void setStartIconPadding(int startIconPadding) {
        mStartView.setCompoundDrawablePadding(startIconPadding);
    }

    private void setEndIconPadding(int endIconPadding) {
        mEndView.setCompoundDrawablePadding(endIconPadding);
    }

    private void setTitle(TextView view, CharSequence text) {
        view.setText(text);
    }

    private void setStartIcon(Drawable drawable) {
        if (mStartIconSize != -1) {
            drawable.setBounds(0, 0, mStartIconSize, mStartIconSize);
        } else {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
        mStartView.setCompoundDrawables(drawable, null, null, null);
    }

    private void setEndIcon(Drawable drawable) {
        if (mEndIconSize != -1) {
            drawable.setBounds(0, 0, mEndIconSize, mEndIconSize);
        } else {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
        mEndView.setCompoundDrawables(null, null, drawable, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置 titlebar 的默认高度
        final int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
            final int heightSpec = MeasureSpec.makeMeasureSpec(ScreenUtils.getActionBarHeight(getContext()), MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 设置点击事件
        mStartView.setOnClickListener(this);
        mTitleView.setOnClickListener(this);
        mEndView.setOnClickListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        mStartView.setOnClickListener(null);
        mTitleView.setOnClickListener(null);
        mEndView.setOnClickListener(null);
        super.onDetachedFromWindow();
    }

    public void setOnTitleBarListener(OnAMZTitleBarListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mListener == null) {
            return;
        }
        if (v == mStartView) {
            mListener.onStartClick(v);
        } else if (v == mTitleView) {
            mListener.onTitleClick(v);
        } else if (v == mEndView) {
            mListener.onEndClick(v);
        }
    }

    static class Builder {

        private final LinearLayout mMainLayout;
        private final TextView mStartView;
        private final TextView mTitleView;
        private final TextView mEndView;
        private final View mLineView;

        Builder(@NonNull Context context) {
            mMainLayout = new LinearLayout(context);
            mMainLayout.setOrientation(LinearLayout.HORIZONTAL);
            mMainLayout.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            mStartView = new TextView(context);
            mStartView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            mStartView.setCompoundDrawablePadding(ScreenUtils.dp2px(context, 5));
            mStartView.setGravity(Gravity.CENTER_VERTICAL);
            mStartView.setSingleLine(true);
            mStartView.setEllipsize(TextUtils.TruncateAt.END);
            mStartView.setEnabled(false);

            mTitleView = new TextView(context);
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
            titleParams.weight = 1;
            titleParams.leftMargin = ScreenUtils.dp2px(context, 10);
            titleParams.rightMargin = ScreenUtils.dp2px(context, 10);
            mTitleView.setLayoutParams(titleParams);
            mTitleView.setGravity(Gravity.CENTER);
            mTitleView.setSingleLine(true);
            mTitleView.setEllipsize(TextUtils.TruncateAt.END);
            mTitleView.setEnabled(false);

            mEndView = new TextView(context);
            mEndView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            mEndView.setCompoundDrawablePadding(ScreenUtils.dp2px(context, 5));
            mEndView.setGravity(Gravity.CENTER_VERTICAL);
            mEndView.setSingleLine(true);
            mEndView.setEllipsize(TextUtils.TruncateAt.END);
            mEndView.setEnabled(false);

            mLineView = new View(context);
            FrameLayout.LayoutParams lineParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            lineParams.gravity = Gravity.BOTTOM;
            mLineView.setLayoutParams(lineParams);
        }

        LinearLayout getMainLayout() {
            return mMainLayout;
        }

        TextView getStartView() {
            return mStartView;
        }

        TextView getTitleView() {
            return mTitleView;
        }

        TextView getEndView() {
            return mEndView;
        }

        View getLineView() {
            return mLineView;
        }

    }


}
