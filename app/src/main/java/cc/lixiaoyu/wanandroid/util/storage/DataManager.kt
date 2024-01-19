package cc.lixiaoyu.wanandroid.util.storage

object DataManager {

    private val dbHelper = DBHelper()
    private val spHelper = SPHelper()

    fun addHistoryData(historyData: String?): Boolean {
        return dbHelper.addHistoryData(historyData)
    }

    fun deleteHistoryData(historyData: String?) {
        dbHelper.deleteHistoryData(historyData)
    }

    fun clearAllHistoryData() {
        dbHelper.clearAllHistoryData()
    }

    fun loadAllHistoryData(): List<String> {
        return dbHelper.loadAllHistoryData()
    }

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