package cc.lixiaoyu.wanandroid.core.main;

import android.widget.Toast;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.app.MyApplication;
import cc.lixiaoyu.wanandroid.event.AutoLoginEvent;
import cc.lixiaoyu.wanandroid.event.LoginEvent;
import cc.lixiaoyu.wanandroid.ui.activity.MainActivity;
import cc.lixiaoyu.wanandroid.util.BaseModelFactory;
import cc.lixiaoyu.wanandroid.util.RetrofitHelper;
import cc.lixiaoyu.wanandroid.util.RxBus;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class MainPresenter extends MainContract.Presenter {
    @Override
    public void start() {
        registEvent();
    }

    private void registEvent() {
        /**
         * 注册登录和自动登录的观察者
         */
        addSubscriber(RxBus.getInstance()
                .toFlowable(LoginEvent.class)
                .filter(new Predicate<LoginEvent>() {
                    @Override
                    public boolean test(LoginEvent loginEvent) throws Exception {
                        return loginEvent.isLogin();
                    }
                })
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(LoginEvent loginEvent) throws Exception {
                        Toast.makeText(MyApplication.getGlobalContext(), "接受到event", Toast.LENGTH_SHORT).show();
                        getView().showLoginView();
                    }
                }));
        addSubscriber(RxBus.getInstance()
                .toFlowable(LoginEvent.class)
                .filter(new Predicate<LoginEvent>() {
                    @Override
                    public boolean test(LoginEvent loginEvent) throws Exception {
                        return !loginEvent.isLogin();
                    }
                })
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(LoginEvent loginEvent) throws Exception {
                        getView().showLogoutView();
                    }
                }));
        addSubscriber(RxBus.getInstance()
                .toFlowable(AutoLoginEvent.class)
                .subscribe(new Consumer<AutoLoginEvent>() {
                    @Override
                    public void accept(AutoLoginEvent event) throws Exception {
                        getView().showAutoLoginView();
                    }
                }));
    }

    @Override
    public void logout() {
        WanAndroidService service = RetrofitHelper.getInstance().getWanAndroidService();
        BaseModelFactory.compose(service.logout()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                mDataManager.setLoginAccount("");
                mDataManager.setLoginPassword("");
                mDataManager.setFirstStartState(false);
                RxBus.getInstance().post(new LoginEvent(false));
                ToastUtil.showToast("退出登录成功");
            }
        });
    }

    @Override
    public boolean getLoginState() {
        return mDataManager.getLoginState();
    }

    @Override
    public String getLoginAccount() {
        return mDataManager.getLoginAccount();
    }
}
