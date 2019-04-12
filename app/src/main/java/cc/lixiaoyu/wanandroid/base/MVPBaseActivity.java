package cc.lixiaoyu.wanandroid.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class MVPBaseActivity<P extends BasePresenter>
        extends BaseActivity implements BaseView{

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
