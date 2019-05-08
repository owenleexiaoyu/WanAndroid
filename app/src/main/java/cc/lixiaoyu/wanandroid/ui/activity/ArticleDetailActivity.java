package cc.lixiaoyu.wanandroid.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.base.BasePresenter;
import cc.lixiaoyu.wanandroid.base.BaseSwipeBackActivity;

/**
 * 文章详情页
 */
public class ArticleDetailActivity extends BaseSwipeBackActivity {
    @BindView(R.id.detail_webview)
    WebView mWebView;
    @BindView(R.id.detail_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.detail_progressbar)
    ProgressBar mProgressBar;

    private String mTitle;
    private String mUrl;

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mTitle = intent.getStringExtra("title");
    }

    @Override
    protected void initView() {
        mToolbar.setTitle(mTitle);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(mUrl);
    }

    @Override
    protected int attachLayout() {
        return R.layout.activity_detail;
    }

    /**
     * 自定义WebViewClient，在网页开始加载时显示进度条，加载完成后隐藏进度条
     */
    class MyWebViewClient extends WebViewClient{
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
        }
    }



    /**
     * 从其他Activity跳转到本Activity，代替startActivity
     * @param context
     * @param title
     * @param url
     */
    public static void actionStart(Context context, String title, String url){
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
}
