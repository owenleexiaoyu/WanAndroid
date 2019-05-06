package cc.lixiaoyu.wanandroid.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;

public class SystemAdapter extends BaseQuickAdapter<PrimaryClass, SystemAdapter.ViewHolder>{

    public SystemAdapter(int layoutResId, @Nullable List<PrimaryClass> data) {
        super(layoutResId, data);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder helper, PrimaryClass item) {
        StringBuffer sb = new StringBuffer();
        helper.tvTitle.setText(item.getName());
        for(PrimaryClass.SubClass sc : item.getChildren()){
            sb.append(sc.getName()+"   ");
        }
        helper.tvContent.setText(sb.toString());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends BaseViewHolder{
        @BindView(R.id.item_system_one_title)
        TextView tvTitle;
        @BindView(R.id.item_system_one_content)
        TextView tvContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
