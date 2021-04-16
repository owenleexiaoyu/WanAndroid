package cc.lixiaoyu.wanandroid.core.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import org.litepal.LitePal;

import butterknife.BindView;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.base.BaseFragment;
import cc.lixiaoyu.wanandroid.entity.User;
import cc.lixiaoyu.wanandroid.util.RetrofitHelper;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegisterFragment extends BaseFragment implements View.OnClickListener {
    public static final String REGISTER_FRAGMENT_TAG = "RegisterFragment";

    @BindView(R.id.register_et_username)
    TextInputEditText mEtUserName;
    @BindView(R.id.register_et_password)
    TextInputEditText mEtPassword;
    @BindView(R.id.register_et_password_again)
    TextInputEditText mEtPwdAgain;
    @BindView(R.id.register_btn_register)
    Button mBtnRegister;
    @BindView(R.id.register_tv_tologin)
    TextView mTvToLogin;

    private LoginActivity mActivity;

    public static RegisterFragment newInstance(){
        return new RegisterFragment();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        mBtnRegister.setOnClickListener(this);
        mTvToLogin.setOnClickListener(this);
    }

    @Override
    protected int attachLayout() {
        return R.layout.fragment_register;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (LoginActivity) getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_btn_register:
                String userName = mEtUserName.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();
                String passwordAgain = mEtPwdAgain.getText().toString().trim();
                WanAndroidService service = RetrofitHelper.getInstance().getWanAndroidService();
                service.register(userName, password, passwordAgain)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            if (result.getErrorCode() == 0) {
                                ToastUtil.showToast("注册成功！");
                                User me = result.getData();
                                //保存到本地数据库
                                LitePal.deleteAll(User.class);
                                me.save();
                                //退出注册界面
                                getActivity().finish();
                            }
                        }, t -> {
                            ToastUtil.showToast("注册失败");
                            t.printStackTrace();
                        });
                break;
            case R.id.register_tv_tologin:
                mActivity.switchFragment(REGISTER_FRAGMENT_TAG, LoginFragment.LOGIN_FRAGMENT_TAG);
                break;
        }
    }
}
