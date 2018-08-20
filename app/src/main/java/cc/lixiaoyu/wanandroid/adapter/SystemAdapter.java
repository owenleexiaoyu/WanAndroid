package cc.lixiaoyu.wanandroid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;

public class SystemAdapter extends RecyclerView.Adapter<SystemAdapter.ViewHolder>{

    private List<PrimaryClass> mDataList;
    private Context mContext;

    private OnSystemItemClickListener mListener;
    public SystemAdapter(Context context, List<PrimaryClass> mDataList) {
        this.mContext = context;
        this.mDataList = mDataList;
    }

    public void setItemClickListener(OnSystemItemClickListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_system_one,viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        StringBuffer sb = new StringBuffer();
        PrimaryClass pc = mDataList.get(i);
        viewHolder.tvTitle.setText(pc.getName());
        for(PrimaryClass.SubClass sc : pc.getChildren()){
            sb.append(sc.getName()+"   ");
        }
        viewHolder.tvContent.setText(sb.toString());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onItemClick(view, i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_system_one_title)
        TextView tvTitle;
        @BindView(R.id.item_system_one_content)
        TextView tvContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public interface OnSystemItemClickListener{
        void onItemClick(View view,int position);
    }
}
