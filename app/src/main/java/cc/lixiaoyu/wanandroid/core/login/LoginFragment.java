package cc.lixiaoyu.wanandroid.core.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.base.BaseFragment;
import cc.lixiaoyu.wanandroid.entity.User;
import cc.lixiaoyu.wanandroid.core.login.event.LoginEvent;
import cc.lixiaoyu.wanandroid.util.storage.DataManager;
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager;
import cc.lixiaoyu.wanandroid.util.RxBus;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginFragment extends BaseFragment implements View.OnClickListener {
    public static final String LOGIN_FRAGMENT_TAG = "LoginFragment";

    @BindView(R.id.login_et_username)
    TextInputEditText mEtUserName;
    @BindView(R.id.login_et_password)
    TextInputEditText mEtPassword;
    @BindView(R.id.login_btn_login)
    Button mBtnLogin;
    @BindView(R.id.login_tv_toregister)
    TextView mTvToRegister;

    private LoginActivity mActivity;

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        mBtnLogin.setOnClickListener(this);
        mTvToRegister.setOnClickListener(this);
    }

    @Override
    protected int attachLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (LoginActivity) getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn_login:
                final String userName = mEtUserName.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();
                WanAndroidService service = RetrofitManager.getInstance().getWanAndroidService();
                service.login(userName, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            if (result.getErrorCode() == 0) {
                                ToastUtil.showToast("登录成功！");
                                User me = result.getData();
                                DataManager manager = new DataManager();
                                manager.setLoginState(true);
                                manager.setLoginAccount(me.getUsername());
                                manager.setLoginPassword(me.getPassword());
                                RxBus.getInstance().post(new LoginEvent(true));
                                //退出登陆界面
                                getActivity().finish();

                            }
                        }, t -> {
                            ToastUtil.showToast("登录失败");
                            t.printStackTrace();
                        });

                break;
            case R.id.login_tv_toregister:
                mActivity.switchFragment(LOGIN_FRAGMENT_TAG, RegisterFragment.REGISTER_FRAGMENT_TAG);
                break;
        }
    }
}
