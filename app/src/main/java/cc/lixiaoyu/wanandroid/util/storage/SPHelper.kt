package cc.lixiaoyu.wanandroid.util.storage

import cc.lixiaoyu.wanandroid.util.AppConst
import cc.lixiaoyu.wanandroid.util.storage.SPUtil.getData
import cc.lixiaoyu.wanandroid.util.storage.SPUtil.saveData

/**
 * SPHelper的实现类
 */
class SPHelper {

    var loginAccount: String?
        get() = getData(AppConst.SP_KEY_LOGIN_ACCOUNT, "") as String?
        set(account) {
            saveData(AppConst.SP_KEY_LOGIN_ACCOUNT, account!!)
        }

    var loginPassword: String?
        get() = getData(AppConst.SP_KEY_LOGIN_PASSWORD, "") as String?
        set(password) {
            saveData(AppConst.SP_KEY_LOGIN_PASSWORD, password!!)
        }

    var loginState: Boolean
        get() = getData(AppConst.SP_KEY_LOGIN_STATE, false) as Boolean
        set(isLogin) {
            saveData(AppConst.SP_KEY_LOGIN_STATE, isLogin)
        }
}