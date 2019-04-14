package cc.lixiaoyu.wanandroid.ui.activity;




import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.base.BaseSwipeBackActivity;

public class AboutActivity extends BaseSwipeBackActivity {
    @BindView(R.id.about_toolbar)
    Toolbar mToolbar;
    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    protected int attachLayout() {
        return R.layout.activity_about;
    }
}
