package cc.lixiaoyu.wanandroid.adapter;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;

public class SearchResultAdapter extends BaseQuickAdapter<ArticlePage.Article, SearchResultAdapter.ViewHolder> {

    public SearchResultAdapter(int layoutResId, @Nullable List<ArticlePage.Article> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(ViewHolder holder, ArticlePage.Article article) {
        holder.tvAuthor.setText(article.getAuthor());
        //处理关键词高亮
        String title = article.getTitle();

        int start = title.indexOf("<em class='highlight'>");
        title = title.replace("<em class='highlight'>","");
        int end = title.indexOf("</em>");
        title = title.replace("</em>","");
        SpannableString ss = new SpannableString(title);
        if(start >=0 && end >start && end <= title.length()){
            ss.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.tvTitle.setText(ss);
    }


    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.item_sresult_author)
        TextView tvAuthor;
        @BindView(R.id.item_sresult_title)
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
