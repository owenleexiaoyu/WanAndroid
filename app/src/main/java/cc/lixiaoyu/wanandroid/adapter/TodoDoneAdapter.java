package cc.lixiaoyu.wanandroid.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.entity.TodoEntity;

public class TodoDoneAdapter extends BaseQuickAdapter<TodoEntity.TodoItem, TodoDoneAdapter.ViewHolder> {

    public TodoDoneAdapter(int layoutResId, @Nullable List<TodoEntity.TodoItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(ViewHolder holder, TodoEntity.TodoItem item) {
        int position = holder.getAdapterPosition();
        if(position != 0 && getData().get(position - 1).getDate() == item.getDate()){
            holder.tvTime.setVisibility(View.GONE);
            holder.vwLine.setVisibility(View.GONE);
        }else{
            holder.tvTime.setText(item.getDateStr());
            holder.tvTime.setVisibility(View.VISIBLE);
            holder.vwLine.setVisibility(View.VISIBLE);
        }
        holder.tvTitle.setText(item.getTitle());
        if(!TextUtils.isEmpty(item.getContent())){
            holder.tvDesc.setText(item.getContent());
            holder.tvDesc.setVisibility(View.VISIBLE);
        }else{
            holder.tvDesc.setVisibility(View.GONE);
        }
        holder.addOnClickListener(R.id.item_done_btn_unfinish);
        holder.addOnClickListener(R.id.item_done_btn_delete);
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.item_done_tv_time)
        TextView tvTime;
        @BindView(R.id.item_done_line)
        View vwLine;
        @BindView(R.id.item_done_tv_title)
        TextView tvTitle;
        @BindView(R.id.item_done_tv_desc)
        TextView tvDesc;
        @BindView(R.id.item_done_btn_unfinish)
        Button btnUnFinish;
        @BindView(R.id.item_done_btn_delete)
        Button btnDelete;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
