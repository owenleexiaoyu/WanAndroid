package cc.lixiaoyu.wanandroid.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.lixiaoyu.wanandroid.entity.ArticlePage;

public class MyAdaper extends BaseQuickAdapter<ArticlePage.Article, BaseViewHolder> {

    public MyAdaper(int layoutResId, @Nullable List<ArticlePage.Article> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticlePage.Article item) {

    }
}
