package cc.lixiaoyu.wanandroid.entity;

public class UserState {
    private boolean isLogin = false;

    public UserState(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
