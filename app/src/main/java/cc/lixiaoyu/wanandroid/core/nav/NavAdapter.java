package cc.lixiaoyu.wanandroid.core.nav;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.app.MyApplication;
import cc.lixiaoyu.wanandroid.entity.Nav;

public class NavAdapter extends BaseQuickAdapter<Nav, NavAdapter.ViewHolder> {

    private int mCurrentItem = 0;

    public NavAdapter(int layoutResId, @Nullable List<Nav> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(ViewHolder holder, Nav item) {
        Context ctx = holder.itemView.getContext();
        holder.tvTitle.setText(item.getName());
        if (item == getData().get(mCurrentItem)) {
            holder.tvTitle.setTextColor(ContextCompat.getColor(ctx, R.color.Accent));
        } else {
            holder.tvTitle.setTextColor(ContextCompat.getColor(ctx, R.color.TextPrimary));
        }
    }

    public int getCurrentItem() {
        return mCurrentItem;
    }

    public void setCurrentItem(int currentItem) {
        this.mCurrentItem = currentItem;
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.item_nav_title)
        TextView tvTitle;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
