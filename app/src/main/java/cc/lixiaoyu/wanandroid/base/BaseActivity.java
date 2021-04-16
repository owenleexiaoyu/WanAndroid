package cc.lixiaoyu.wanandroid.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.lixiaoyu.wanandroid.util.DataManager;

/**
 * 基类Activity，处理一些公共逻辑
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(attachLayout());
        unbinder = ButterKnife.bind(this);
        initData();
        initView();
    }

    /**
     * 解绑ButterKnife
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null && unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化View控件
     */
    protected abstract void initView();

    /**
     * 加载布局文件
     *
     * @return
     */
    protected abstract int attachLayout();

    /**
     * 处理返回按钮事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    /**
     * 判断用户是否登录
     *
     * @return
     */
    protected boolean isLogined() {
        return new DataManager().getLoginState();
    }
}
