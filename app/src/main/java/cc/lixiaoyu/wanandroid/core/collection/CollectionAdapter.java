package cc.lixiaoyu.wanandroid.core.collection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.entity.Article;

public class CollectionAdapter extends BaseQuickAdapter<Article, CollectionAdapter.ViewHolder> {

    public CollectionAdapter(int layoutResId, @Nullable List<Article> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(ViewHolder holder, Article article) {
        holder.tvAuthor.setText(article.getAuthor());
        holder.tvTime.setText(article.getNiceDate());
        holder.tvTitle.setText(article.getTitle());
        holder.tvChapter.setText(article.getChapterName());
        holder.imgCollect.setImageResource(R.drawable.ic_heart_orange);
        holder.addOnClickListener(R.id.item_collection_collect);
    }


    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.item_collection_author)
        TextView tvAuthor;
        @BindView(R.id.item_collection_time)
        TextView tvTime;
        @BindView(R.id.item_collection_title)
        TextView tvTitle;
        @BindView(R.id.item_collection_chapter)
        TextView tvChapter;
        @BindView(R.id.item_collection_collect)
        ImageView imgCollect;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
