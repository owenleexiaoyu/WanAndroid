package cc.lixiaoyu.wanandroid.core.main;

import cc.lixiaoyu.wanandroid.base.mvp.BaseModel;
import cc.lixiaoyu.wanandroid.base.mvp.BasePresenter;
import cc.lixiaoyu.wanandroid.base.mvp.BaseView;

public interface MainContract {
    interface Model extends BaseModel{

    }
    interface View extends BaseView{
        /**
         * 展示登录后的布局
         */
        void showLoginView();
        /**
         * 展示未登录的布局
         */
        void showLogoutView();
        /**
         * 展示自动登录的布局
         */
        void showAutoLoginView();
    }

    abstract class Presenter extends BasePresenter<View>{
        /**
         * 退出登录
         */
        public abstract void logout();

        /**
         * 获取登录状态
         * @return
         */
        public abstract boolean getLoginState();

        /**
         * 获取账号
         * @return
         */
        public abstract String getLoginAccount();
    }
}
