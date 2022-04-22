package cc.lixiaoyu.wanandroid.util.storage;

/**
 * SharedPreference 帮助类，提供应用中需要使用SharedPreference存储的数据方法
 *
 */
public interface SPHelper {

    /**
     * 存储账号
     * @param account
     */
    void setLoginAccount(String account);

    /**
     * 获得账号
     * @return
     */
    String getLoginAccount();

    /**
     * 存储密码
     * @param password
     */
    void setLoginPassword(String password);

    /**
     * 获得密码
     * @return
     */
    String getLoginPassword();

    /**
     * 存储登录状态
     * @param isLogin
     */
    void setLoginState(boolean isLogin);

    /**
     * 获得登录状态
     * @return
     */
    boolean getLoginState();

    /**
     * 存储夜间模式状态
     * @param nightMode
     */
    void setNightModeState(boolean nightMode);

    /**
     * 获得夜间模式状态
     * @return
     */
    boolean getNightModeState();

    /**
     * 存储是否首次启动状态
     * @param isFirst
     */
    void setFirstStartState(boolean isFirst);

    /**
     * 获得首次启动状态
     * @return
     */
    boolean getFirstStartState();

    /**
     * 存储无图模式状态
     * @param noImage
     */
    void setNoImageState(boolean noImage);

    /**
     * 获得无图模式状态
     * @return
     */
    boolean getNoImageState();
}
