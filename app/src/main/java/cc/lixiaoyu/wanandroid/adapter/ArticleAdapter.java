package cc.lixiaoyu.wanandroid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private List<ArticlePage.Article> articleList;
    private Context context;
    private OnArticleItemClickListener mListener;
    public ArticleAdapter(Context context, List<ArticlePage.Article> articleList) {
        this.articleList = articleList;
        this.context = context;
    }

    public void setArticleItemClickListener(OnArticleItemClickListener listener){
        mListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_article, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        ArticlePage.Article article = articleList.get(i);
        viewHolder.tvAuther.setText(article.getAuthor());
        Date date = new Date(article.getPublishTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        viewHolder.tvTime.setText(format.format(date));
        if(TextUtils.isEmpty(article.getEnvelopePic())){
            viewHolder.imgPhoto.setVisibility(View.GONE);
        }else{
            viewHolder.imgPhoto.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions()
                    .centerCrop();
            Glide.with(context).load(article.getEnvelopePic()).apply(options).into(viewHolder.imgPhoto);
        }
        viewHolder.tvTitle.setText(article.getTitle());
        viewHolder.tvChapter.setText(article.getChapterName());
        //为item设置点击事件监听
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onItemClick(view, i);
                }
            }
        });
        viewHolder.imgCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onCollectBtnClick(view, i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_article_auther)
        TextView tvAuther;
        @BindView(R.id.item_article_time)
        TextView tvTime;
        @BindView(R.id.item_article_img)
        ImageView imgPhoto;
        @BindView(R.id.item_article_title)
        TextView tvTitle;
        @BindView(R.id.item_article_chapter)
        TextView tvChapter;
        @BindView(R.id.item_article_collect)
        ImageView imgCollect;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnArticleItemClickListener{
        void onItemClick(View view,int position);
        void onCollectBtnClick(View view, int position);
    }
}
