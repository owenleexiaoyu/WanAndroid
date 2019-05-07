package cc.lixiaoyu.wanandroid.ui.activity;




import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import butterknife.BindView;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.base.BaseSwipeBackActivity;

public class AboutActivity extends BaseSwipeBackActivity {
    @BindView(R.id.about_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.about_webview)
    WebView mWebView;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //初始化webview
        mWebView.loadUrl("file:///android_asset/about_us.html");
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);
    }

    @Override
    protected int attachLayout() {
        return R.layout.activity_about;
    }
}
