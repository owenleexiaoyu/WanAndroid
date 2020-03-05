package cc.lixiaoyu.wanandroid.base;

import android.os.Bundle;
import androidx.annotation.Nullable;

public abstract class MVPBaseFragment<P extends BasePresenter> extends BaseFragment implements BaseView{

    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        mPresenter.attachView(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    protected abstract P createPresenter();
}
