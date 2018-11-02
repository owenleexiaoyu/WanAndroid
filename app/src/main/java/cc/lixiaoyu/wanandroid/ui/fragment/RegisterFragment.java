package cc.lixiaoyu.wanandroid.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.litepal.LitePal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.User;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.ui.activity.LoginActivity;
import cc.lixiaoyu.wanandroid.util.RetrofitUtil;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment implements View.OnClickListener {
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

    private Unbinder unbinder;
    private LoginActivity mActivity;

    public static RegisterFragment newInstance(){
        return new RegisterFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (LoginActivity) getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnRegister.setOnClickListener(this);
        mTvToLogin.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_btn_register:
                String userName = mEtUserName.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();
                String passwordAgain = mEtPwdAgain.getText().toString().trim();
                WanAndroidService service = RetrofitUtil.getWanAndroidService();
                Call<WanAndroidResult<User>> call = service.register(userName, password, passwordAgain);
                call.enqueue(new Callback<WanAndroidResult<User>>() {
                    @Override
                    public void onResponse(Call<WanAndroidResult<User>> call,
                                           Response<WanAndroidResult<User>> response) {
                        WanAndroidResult<User> result = response.body();
                        if(result.getErrorCode() == 0){
                            ToastUtil.showToast("注册成功！");
                            User me = result.getData();
                            //保存到本地数据库
                            LitePal.deleteAll(User.class);
                            me.save();
                            //退出注册界面
                            getActivity().finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<WanAndroidResult<User>> call, Throwable t) {
                        ToastUtil.showToast("注册失败");
                        t.printStackTrace();
                    }
                });
                break;
            case R.id.register_tv_tologin:
                mActivity.switchFragment(REGISTER_FRAGMENT_TAG, LoginFragment.LOGIN_FRAGMENT_TAG);
                break;
        }
    }
}
