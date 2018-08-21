package cc.lixiaoyu.wanandroid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITEM_TYPE_FOOTER = 1;
    private List<ArticlePage.Article> articleList;
    private Context context;
    private OnArticleItemClickListener mListener;

    private boolean hasMore;  //上拉加载时，是否有更多数据
    private boolean isFooterHided; // 是否隐藏footer


    public ArticleAdapter(Context context, List<ArticlePage.Article> articleList) {
        this.articleList = articleList;
        this.context = context;
    }

    public void setArticleItemClickListener(OnArticleItemClickListener listener){
        mListener = listener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if(viewType == ITEM_TYPE_NORMAL){
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_recyclerview_article, viewGroup, false);
            NormalViewHolder holder = new NormalViewHolder(view);
            return holder;
        }else if(viewType == ITEM_TYPE_FOOTER){
            view = LayoutInflater.from(context)
                    .inflate(R.layout.layout_list_footer, viewGroup, false);
            FooterViewHolder holder = new FooterViewHolder(view);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        if(viewHolder instanceof NormalViewHolder){
            NormalViewHolder holder = (NormalViewHolder) viewHolder;
            ArticlePage.Article article = articleList.get(i);
            holder.tvAuther.setText(article.getAuthor());
            Date date = new Date(article.getPublishTime());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            holder.tvTime.setText(format.format(date));
            if(TextUtils.isEmpty(article.getEnvelopePic())){
                holder.imgPhoto.setVisibility(View.GONE);
            }else{
                holder.imgPhoto.setVisibility(View.VISIBLE);
                RequestOptions options = new RequestOptions()
                        .centerCrop();
                Glide.with(context).load(article.getEnvelopePic())
                        .apply(options).into(holder.imgPhoto);
            }
            holder.tvTitle.setText(article.getTitle());
            holder.tvChapter.setText(article.getChapterName());
            //为item设置点击事件监听
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        mListener.onItemClick(view, i);
                    }
                }
            });
            holder.imgCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        mListener.onCollectBtnClick(view, i);
                    }
                }
            });
        }else{
            FooterViewHolder holder = (FooterViewHolder) viewHolder;
            holder.itemView.setVisibility(View.VISIBLE);
//            if(hasMore){
//                //显示footer
//                isFooterHided = false;
//                holder.tvTip.setText("正在加载更多...");
//            }else{
//                holder.tvTip.setText("没有更多数据了");
//                holder.itemView.setVisibility(View.GONE);
//                isFooterHided = true;
//            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == articleList.size()){
            return ITEM_TYPE_FOOTER;
        }else{
            return ITEM_TYPE_NORMAL;
        }
    }


    /**
     * 获取列表中item条目，加1是因为加上了一个footerview
     * @return
     */
    @Override
    public int getItemCount() {
        return articleList.size()+1;
    }

    /**
     * 获取真实的数据条目
     * @return
     */
    public int getRealItemCount(){
        return articleList.size();
    }

    class NormalViewHolder extends RecyclerView.ViewHolder {
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
        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.footer_progressbar)
        ProgressBar progressBar;
        @BindView(R.id.footer_tip)
        TextView tvTip;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnArticleItemClickListener{
        void onItemClick(View view,int position);
        void onCollectBtnClick(View view, int position);
    }

    /**
     * 更新数据的方法
     * @param newList
     * @param hasMore
     */
    public void updateList(List<ArticlePage.Article> newList, boolean hasMore){
        if(newList != null){
            articleList.addAll(newList);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }


    class ArticleSrcollListener extends RecyclerView.OnScrollListener {
        private LinearLayoutManager layoutManager;
        private ArticleAdapter adapter;
        private int lastVisibleItem = 0;
        public ArticleSrcollListener(LinearLayoutManager layoutManager){
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if(newState == RecyclerView.SCROLL_STATE_IDLE){

            }
        }
    }
}
