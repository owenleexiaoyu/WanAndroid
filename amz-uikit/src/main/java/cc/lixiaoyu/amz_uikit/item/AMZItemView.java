package cc.lixiaoyu.amz_uikit.item;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.lixiaoyu.amz_uikit.R;

public class AMZItemView extends RelativeLayout {

    private Drawable mStartIcon;
    private String mStartText;

    private ImageView mStartIconView;
    private TextView mStartTextView;

    public AMZItemView(Context context) {
        this(context, null, 0);
    }

    public AMZItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AMZItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initStyle(context, attrs);
    }

    private void initStyle(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AMZItemView);
        if (ta.hasValue(R.styleable.AMZItemView_start_text)) {
            setStartText(ta.getString(R.styleable.AMZItemView_start_text));
        }
        if(ta.hasValue(R.styleable.AMZItemView_start_icon)) {
            setStartIcon(ta.getDrawable(R.styleable.AMZItemView_start_icon));
        }
        ta.recycle();
    }

    private void initView(Context context) {
        final View view = LayoutInflater.from(context)
                .inflate(R.layout.amz_item_view, this, true);
        mStartIconView = view.findViewById(R.id.start_icon);
        mStartTextView = view.findViewById(R.id.start_text);
    }

    public void setStartIcon(Drawable drawable) {
        mStartIconView.setImageDrawable(drawable);
    }

    public void setStartText(CharSequence text) {
        mStartTextView.setText(text);
    }

}
