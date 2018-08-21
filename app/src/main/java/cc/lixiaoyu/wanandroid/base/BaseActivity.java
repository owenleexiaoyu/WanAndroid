package cc.lixiaoyu.wanandroid.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * 基类Activity，处理一些公共逻辑
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(attachLayout());
        initData();
        initView();
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
     * @return
     */
    protected abstract int attachLayout();

    /**
     * 处理返回按钮事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    /**
     * 判断用户是否登录
     * @return
     */
    protected boolean isLogined(){
        return false;
    }
}
