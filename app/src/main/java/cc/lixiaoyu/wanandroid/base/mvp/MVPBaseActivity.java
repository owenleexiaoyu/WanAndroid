package cc.lixiaoyu.wanandroid.base.mvp;

import android.os.Bundle;
import androidx.annotation.Nullable;

import cc.lixiaoyu.wanandroid.base.BaseActivity;
import cc.lixiaoyu.wanandroid.base.mvp.BasePresenter;
import cc.lixiaoyu.wanandroid.base.mvp.BaseView;

/**
 * MVP 形式的 BaseActivity
 * @param <P>
 */
public abstract class MVPBaseActivity<P extends BasePresenter>
        extends BaseActivity implements BaseView {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        mPresenter.attachView(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    protected abstract P createPresenter();
}
