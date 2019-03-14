package cc.lixiaoyu.wanandroid.util;

/**
 * SPHelper的实现类
 */
public class SPHelperImpl implements SPHelper {
    @Override
    public void setLoginAccount(String account) {
        SPUtil.saveData(AppConst.SP_KEY_LOGIN_ACCOUNT, account);
    }

    @Override
    public String getLoginAccount() {
        return (String) SPUtil.getData(AppConst.SP_KEY_LOGIN_ACCOUNT, "");
    }

    @Override
    public void setLoginPassword(String password) {
        SPUtil.saveData(AppConst.SP_KEY_LOGIN_PASSWORD, password);
    }

    @Override
    public String getLoginPassword() {
        return (String) SPUtil.getData(AppConst.SP_KEY_LOGIN_PASSWORD, "");
    }

    @Override
    public void setLoginState(boolean isLogin) {
        SPUtil.saveData(AppConst.SP_KEY_LOGIN_STATE, isLogin);
    }

    @Override
    public boolean getLoginState() {
        return (boolean) SPUtil.getData(AppConst.SP_KEY_LOGIN_STATE, false);
    }

    @Override
    public void setNightModeState(boolean nightMode) {
        SPUtil.saveData(AppConst.SP_KEY_NOGHT_MODE, nightMode);
    }

    @Override
    public boolean getNightModeState() {
        return (boolean) SPUtil.getData(AppConst.SP_KEY_NOGHT_MODE, false);
    }

    @Override
    public void setFirstStartState(boolean isFirst) {
        SPUtil.saveData(AppConst.SP_KEY_FIRST_START, isFirst);
    }

    @Override
    public boolean getFirstStartState() {
        return (boolean) SPUtil.getData(AppConst.SP_KEY_FIRST_START, false);
    }

    @Override
    public void setNoImageState(boolean noImage) {
        SPUtil.saveData(AppConst.SP_KEY_NO_IMAGE, noImage);
    }

    @Override
    public boolean getNoImageState() {
        return (boolean) SPUtil.getData(AppConst.SP_KEY_NO_IMAGE, false);
    }
}
