package cc.lixiaoyu.wanandroid.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.base.MVPBaseSwipeBackActivity;
import cc.lixiaoyu.wanandroid.core.login.LoginPrsenter;
import cc.lixiaoyu.wanandroid.ui.fragment.LoginFragment;
import cc.lixiaoyu.wanandroid.ui.fragment.RegisterFragment;

public class LoginActivity extends MVPBaseSwipeBackActivity<LoginPrsenter> {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private Fragment loginFragment;
    private Fragment registerFragment;
    private Fragment currentFragment;
    @Override
    protected void initData() {
        loginFragment = LoginFragment.newInstance();
        registerFragment = RegisterFragment.newInstance();
    }

    @Override
    protected void initView() {
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.add(R.id.login_container, loginFragment, LoginFragment.LOGIN_FRAGMENT_TAG)
                    .add(R.id.login_container, registerFragment, RegisterFragment.REGISTER_FRAGMENT_TAG)
                    .hide(registerFragment)
                    .commit();
    }

    @Override
    protected int attachLayout() {
        return R.layout.activity_login;
    }

    /**
     * 在Activity中控制Fragment的切换
     * @param fromTag   当前fragment的tag
     * @param toTag     目标fragment的tag
     */
    public void switchFragment(String fromTag, String toTag){
        Fragment from = mFragmentManager.findFragmentByTag(fromTag);
        Fragment to  = mFragmentManager.findFragmentByTag(toTag);
        if(currentFragment != to){
            currentFragment = to;
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if(!to.isAdded()){
                transaction.hide(from).add(R.id.login_container, to, toTag).commit();
            }else{
                transaction.hide(from).show(to).commit();
            }
        }
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected LoginPrsenter createPresenter() {
        return new LoginPrsenter();
    }
}
