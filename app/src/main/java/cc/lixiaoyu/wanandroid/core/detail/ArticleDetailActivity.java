package cc.lixiaoyu.wanandroid.core.detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;


import butterknife.BindView;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.base.mvp.MVPBaseSwipeBackActivity;
import cc.lixiaoyu.wanandroid.core.login.LoginActivity;
import cc.lixiaoyu.wanandroid.util.ToastUtil;

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

    private DetailParam mDetailParam;

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mDetailParam = (DetailParam) intent.getSerializableExtra(ARTICLE_DETAIL_PARAM);
        if (mDetailParam == null) {
            ToastUtil.showToast("文章参数为空");
            finish();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        mTvTitle.setText(mDetailParam.getTitle());
        mTvTitle.setSelected(true);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(mDetailParam.getLink());
    }

    @Override
    protected int attachLayout() {
        return R.layout.activity_detail;
    }

    @Override
    public void showLoading() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showCollectArticle(boolean success) {
        String toastText = success ? getString(R.string.collect_success) : getString(R.string.collect_fail);
        ToastUtil.showToast(toastText);
    }

    @Override
    public void showUnCollectArticle(boolean success) {
        String toastText = success ? getString(R.string.uncollect_success) : getString(R.string.uncollect_fail);
        ToastUtil.showToast(toastText);
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

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri uri = request.getUrl();
            if ("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) {
                return false;
            } else {
                return true;
            }
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
                tryCollectOrUnCollectArticle();
                break;
            case R.id.menu_detail_share:
                shareArticle(mDetailParam);
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void tryCollectOrUnCollectArticle() {
        if (!mPresenter.getDataManager().getLoginState()) {
            //未登录，前往登陆页面进行登陆操作
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            //登陆后，可以进行文章的收藏与取消收藏操作
            //如果文章已经被收藏了，就取消收藏，如果没有收藏，就收藏
//            if (isCollect()) {
//                mPresenter.unCollectArticle(mDetailParam.getArticleId());
//            } else {
//                mPresenter.collectArticle(mDetailParam.getArticleId());
//            }
        }
    }

    private void shareArticle(DetailParam mDetailParam) {
        String shareMsg = getString(R.string.share_hint) + mDetailParam.getTitle() +
                "（" + mDetailParam.getLink() + "）";
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
    public static void actionStart(Context context, @NonNull DetailParam detailParam) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(ARTICLE_DETAIL_PARAM, detailParam);
        context.startActivity(intent);
    }
}
