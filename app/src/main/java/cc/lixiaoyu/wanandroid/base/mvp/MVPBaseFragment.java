package cc.lixiaoyu.wanandroid.base.mvp;

import android.os.Bundle;
import androidx.annotation.Nullable;

import cc.lixiaoyu.wanandroid.base.BaseFragment;
import cc.lixiaoyu.wanandroid.base.mvp.BasePresenter;
import cc.lixiaoyu.wanandroid.base.mvp.BaseView;

/**
 * MVP 形式的 BaseFragment
 * @param <P>
 */
public abstract class MVPBaseFragment<P extends BasePresenter> extends BaseFragment implements BaseView {

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
