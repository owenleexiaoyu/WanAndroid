package cc.lixiaoyu.wanandroid.core.todo;

import androidx.annotation.Nullable;
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

public class TodoUndoneAdapter extends BaseQuickAdapter<TodoEntity.TodoItem, TodoUndoneAdapter.ViewHolder> {

    public TodoUndoneAdapter(int layoutResId, @Nullable List<TodoEntity.TodoItem> data) {
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
        holder.addOnClickListener(R.id.item_undone_btn_finish);
        holder.addOnClickListener(R.id.item_undone_btn_delete);
        holder.addOnClickListener(R.id.item_undone_tv_title);
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.item_undone_tv_time)
        TextView tvTime;
        @BindView(R.id.item_undone_line)
        View vwLine;
        @BindView(R.id.item_undone_tv_title)
        TextView tvTitle;
        @BindView(R.id.item_undone_tv_desc)
        TextView tvDesc;
        @BindView(R.id.item_undone_btn_finish)
        Button btnFinish;
        @BindView(R.id.item_undone_btn_delete)
        Button btnDelete;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
