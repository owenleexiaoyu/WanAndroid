package cc.lixiaoyu.wanandroid.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.User;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.event.LoginEvent;
import cc.lixiaoyu.wanandroid.ui.activity.LoginActivity;
import cc.lixiaoyu.wanandroid.util.DataManager;
import cc.lixiaoyu.wanandroid.util.RetrofitHelper;
import cc.lixiaoyu.wanandroid.util.RxBus;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements View.OnClickListener {
    public static final String LOGIN_FRAGMENT_TAG = "LoginFragment";
    private static final String TAG = "LoginFragment";

    @BindView(R.id.login_et_username)
    TextInputEditText mEtUserName;
    @BindView(R.id.login_et_password)
    TextInputEditText mEtPassword;
    @BindView(R.id.login_btn_login)
    Button mBtnLogin;
    @BindView(R.id.login_tv_toregister)
    TextView mTvToRegister;

    private Unbinder unbinder;
    private LoginActivity mActivity;

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
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
        mBtnLogin.setOnClickListener(this);
        mTvToRegister.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn_login:
                final String userName = mEtUserName.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();
                WanAndroidService service = RetrofitHelper.getInstance().getWanAndroidService();
                Call<WanAndroidResult<User>> call = service.login(userName, password);
                call.enqueue(new Callback<WanAndroidResult<User>>() {
                    @Override
                    public void onResponse(Call<WanAndroidResult<User>> call,
                                           Response<WanAndroidResult<User>> response) {
                        WanAndroidResult<User> result = response.body();
                        if(result.getErrorCode() == 0){
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
                    }

                    @Override
                    public void onFailure(Call<WanAndroidResult<User>> call, Throwable t) {
                        ToastUtil.showToast("登录失败");
                        t.printStackTrace();
                    }
                });

                break;
            case R.id.login_tv_toregister:
                mActivity.switchFragment(LOGIN_FRAGMENT_TAG, RegisterFragment.REGISTER_FRAGMENT_TAG);
                break;
        }
    }
}
