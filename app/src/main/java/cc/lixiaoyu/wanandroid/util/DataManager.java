package cc.lixiaoyu.wanandroid.util;

import java.util.List;

public class DataManager implements DBHelper, SPHelper {

    private DBHelper mDBHelper = new DBHelperImpl();
    private SPHelper mSPHelper = new SPHelperImpl();

    @Override
    public boolean addHistoryData(String historyData) {
        return mDBHelper.addHistoryData(historyData);
    }

    @Override
    public void deleteHistoryData(String historyData) {
        mDBHelper.deleteHistoryData(historyData);
    }

    @Override
    public void clearAllHistoryData() {
        mDBHelper.clearAllHistoryData();
    }

    @Override
    public List<String> loadAllHistoryData() {
        return mDBHelper.loadAllHistoryData();
    }

    @Override
    public void setLoginAccount(String account) {
        mSPHelper.setLoginAccount(account);
    }

    @Override
    public String getLoginAccount() {
        return mSPHelper.getLoginAccount();
    }

    @Override
    public void setLoginPassword(String password) {
        mSPHelper.setLoginPassword(password);
    }

    @Override
    public String getLoginPassword() {
        return mSPHelper.getLoginPassword();
    }

    @Override
    public void setLoginState(boolean isLogin) {
        mSPHelper.setLoginState(isLogin);
    }

    @Override
    public boolean getLoginState() {
        return mSPHelper.getLoginState();
    }

    @Override
    public void setNightModeState(boolean nightMode) {
        mSPHelper.setNightModeState(nightMode);
    }

    @Override
    public boolean getNightModeState() {
        return mSPHelper.getNightModeState();
    }

    @Override
    public void setFirstStartState(boolean isFirst) {
        mSPHelper.setFirstStartState(isFirst);
    }

    @Override
    public boolean getFirstStartState() {
        return mSPHelper.getFirstStartState();
    }

    @Override
    public void setNoImageState(boolean noImage) {
        mSPHelper.setNoImageState(noImage);
    }

    @Override
    public boolean getNoImageState() {
        return mSPHelper.getNoImageState();
    }
}
