package cc.lixiaoyu.wanandroid.core.projectdata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.entity.Article;

public class ProjectDataAdapter extends BaseQuickAdapter<Article, ProjectDataAdapter.ViewHolder> {

    public ProjectDataAdapter(int layoutResId, @Nullable List<Article> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(ViewHolder holder, Article article) {
        holder.tvAuther.setText(article.getAuthor());
        holder.tvTitle.setText(article.getTitle());
        holder.tvDesc.setText(article.getDesc());
        holder.tvTime.setText(article.getNiceDate());
        if(TextUtils.isEmpty(article.getEnvelopePic())){
            holder.imgPhoto.setVisibility(View.GONE);
        }else{
            holder.imgPhoto.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions()
                    .centerCrop();
            Glide.with(mContext).load(article.getEnvelopePic())
                    .apply(options).into(holder.imgPhoto);
        }
        //设置文章是否被收藏
        if(article.isCollect()){
            holder.imgCollect.setImageResource(R.mipmap.ic_heart_orange);
        }else{
            holder.imgCollect.setImageResource(R.mipmap.ic_heart_gray);
        }
        holder.addOnClickListener(R.id.item_project_collect);
    }


    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.item_project_author)
        TextView tvAuther;
        @BindView(R.id.item_project_time)
        TextView tvTime;
        @BindView(R.id.item_project_img)
        ImageView imgPhoto;
        @BindView(R.id.item_project_title)
        TextView tvTitle;
        @BindView(R.id.item_project_desc)
        TextView tvDesc;
        @BindView(R.id.item_project_collect)
        ImageView imgCollect;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
