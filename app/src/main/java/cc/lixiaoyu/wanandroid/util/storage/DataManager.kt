package cc.lixiaoyu.wanandroid.util.storage

object DataManager {

    private val spHelper = SPHelper()

    var loginAccount: String?
        get() = spHelper.loginAccount
        set(account) {
            spHelper.loginAccount = account
        }

    var loginPassword: String?
        get() = spHelper.loginPassword
        set(password) {
            spHelper.loginPassword = password
        }

    @JvmStatic
    var loginState: Boolean
        get() = spHelper.loginState
        set(isLogin) {
            spHelper.loginState = isLogin
        }

    var isDarkMode: Boolean
        get() = spHelper.isDarkMode
        set(nightMode) {
            spHelper.isDarkMode = nightMode
        }
}