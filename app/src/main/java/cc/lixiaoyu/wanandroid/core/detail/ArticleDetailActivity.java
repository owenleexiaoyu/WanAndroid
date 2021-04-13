package cc.lixiaoyu.wanandroid.core.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.BindView;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.base.MVPBaseSwipeBackActivity;

/**
 * 文章详情页
 */
public class ArticleDetailActivity extends MVPBaseSwipeBackActivity<ArticleDetailContract.Presenter>
        implements ArticleDetailContract.View {

    private static final String ARTICLE_DETAIL_PARAM = "article_detail_param";

    @BindView(R.id.detail_webview)
    WebView mWebView;
    @BindView(R.id.detail_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.detail_toolbar_title)
    TextView mTvTitle;
    @BindView(R.id.detail_progressbar)
    ProgressBar mProgressBar;

    private ArticleDetailParam mArticleDetailParam;

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mArticleDetailParam = (ArticleDetailParam) intent.getSerializableExtra(ARTICLE_DETAIL_PARAM);
    }

    @Override
    protected void initView() {
        mTvTitle.setText(mArticleDetailParam.getTitle());
        mTvTitle.setSelected(true);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(mArticleDetailParam.getLink());
    }

    @Override
    protected int attachLayout() {
        return R.layout.activity_detail;
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showCollectArticle(boolean success) {
        String toastText = success ? "收藏成功" : "收藏失败";
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUnCollectArticle(boolean success) {
        String toastText = success ? "取消收藏成功" : "取消收藏失败";
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected ArticleDetailContract.Presenter createPresenter() {
        return new ArticleDetailPresenter();
    }

    /**
     * 自定义 WebViewClient，在网页开始加载时显示进度条，加载完成后隐藏进度条
     */
    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showLoading();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideLoading();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_detail_collect:
                mPresenter.collectArticle(mArticleDetailParam.getArticleId());
                break;
            case R.id.menu_detail_share:
                shareArticle(mArticleDetailParam);
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void shareArticle(ArticleDetailParam mArticleDetailParam) {
        String shareMsg = "这篇文章很不错，快打开看看吧：" + mArticleDetailParam.getTitle() +
                " -> " + mArticleDetailParam.getLink();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, shareMsg);
        intent.setType("text/plain");
        startActivity(intent);
    }

    /**
     * 从其他Activity跳转到本Activity，代替startActivity
     *
     * @param context
     * @param detailParam
     */
    public static void actionStart(Context context, @NonNull ArticleDetailParam detailParam) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(ARTICLE_DETAIL_PARAM, detailParam);
        context.startActivity(intent);
    }
}
