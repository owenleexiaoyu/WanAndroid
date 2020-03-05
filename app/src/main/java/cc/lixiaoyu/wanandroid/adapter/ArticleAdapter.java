package cc.lixiaoyu.wanandroid.adapter;

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
import cc.lixiaoyu.wanandroid.entity.ArticlePage;

public class ArticleAdapter extends BaseQuickAdapter<ArticlePage.Article, ArticleAdapter.ViewHolder> {

    public ArticleAdapter(int layoutResId, @Nullable List<ArticlePage.Article> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(ViewHolder holder, ArticlePage.Article article) {
        holder.tvAuthor.setText(article.getAuthor());
        holder.tvTime.setText(article.getNiceDate());
        holder.tvTitle.setText(article.getTitle());
        holder.tvChapter.setText(article.getChapterName());
        if(TextUtils.isEmpty(article.getEnvelopePic())){
            holder.imgPhoto.setVisibility(View.GONE);
        }else{
            holder.imgPhoto.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions()
                    .centerCrop();
            Glide.with(mContext).load(article.getEnvelopePic())
                    .apply(options).into(holder.imgPhoto);
        }
        //设置置顶标签的显示和隐藏
        if(article.getType() == 0){
            holder.tvTop.setVisibility(View.GONE);
        }else if(article.getType() == 1){
            holder.tvTop.setVisibility(View.VISIBLE);
        }

        //设置文章是否被收藏
        if(article.isCollect()){
            holder.imgCollect.setImageResource(R.mipmap.ic_heart_orange);
        }else{
            holder.imgCollect.setImageResource(R.mipmap.ic_heart_gray);
        }

        //为收藏按钮添加点击事件
        holder.addOnClickListener(R.id.item_article_collect);
    }


    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.item_article_author)
        TextView tvAuthor;
        @BindView(R.id.item_article_time)
        TextView tvTime;
        @BindView(R.id.item_article_img)
        ImageView imgPhoto;
        @BindView(R.id.item_article_title)
        TextView tvTitle;
        @BindView(R.id.item_article_chapter)
        TextView tvChapter;
        @BindView(R.id.item_article_top)
        TextView tvTop;
        @BindView(R.id.item_article_collect)
        ImageView imgCollect;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
