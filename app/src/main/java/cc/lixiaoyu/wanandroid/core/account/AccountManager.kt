package cc.lixiaoyu.wanandroid.core.account

import android.annotation.SuppressLint
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cc.lixiaoyu.wanandroid.entity.Optional
import cc.lixiaoyu.wanandroid.entity.User
import cc.lixiaoyu.wanandroid.util.RxBus
import cc.lixiaoyu.wanandroid.util.network.BaseModelFactory
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager
import cc.lixiaoyu.wanandroid.util.storage.DataManager

object AccountManager {

    private val dataManager = DataManager()
    private val service = RetrofitManager.getInstance().wanAndroidService

    private val _userLiveData: MutableLiveData<User?> = MutableLiveData()
    var userLiveData: LiveData<User?> = _userLiveData

    init {
        loadUserFromSp()
    }

    // 从 SP 中读取 User 信息
    private fun loadUserFromSp() {
        val loginAccount = dataManager.loginAccount
        val user = if (!loginAccount.isNullOrEmpty()) {
            User().apply {
                username = loginAccount
                password = dataManager.loginPassword
            }
        } else {
            null
        }
        _userLiveData.value = user
    }

    private fun updateUser(user: User?) {
        dataManager.loginAccount = user?.username ?: ""
        if (Looper.getMainLooper() == Looper.myLooper()) {
            _userLiveData.value = user
        } else {
            _userLiveData.postValue(user)
        }
    }

    fun isLogin(): Boolean = _userLiveData.value != null

    @SuppressLint("CheckResult")
    fun signUp(
        userName: String,
        password: String,
        passwordConfirm: String,
        callback: LoginCallback? = null
    ) {
        BaseModelFactory.compose(service.register(userName, password, passwordConfirm))
            .subscribe({ result: Optional<User> ->
                val me = result.get()
                updateUser(me)
                callback?.onSuccess(me)
                RxBus.getInstance().post(LoginEvent(true))
            }) { t: Throwable ->
                callback?.onFail(t)
                t.printStackTrace()
            }
    }

    @SuppressLint("CheckResult")
    fun signIn(
        userName: String,
        password: String,
        callback: LoginCallback? = null
    ) {
        BaseModelFactory.compose(service.login(userName, password))
            .subscribe({ result: Optional<User> ->
                val me = result.get()
                updateUser(me)
                callback?.onSuccess(me)
                RxBus.getInstance().post(LoginEvent(true))
            }) { t: Throwable ->
                callback?.onFail(t)
                t.printStackTrace()
            }
    }

    @SuppressLint("CheckResult")
    fun logout(callback: LogoutCallback? = null) {
        BaseModelFactory.compose(service.logout())
            .subscribe({
                updateUser(null)
                callback?.onSuccess()
                RxBus.getInstance().post(LoginEvent(false))
            }, { t: Throwable ->
                callback?.onFail(t)
                t.printStackTrace()
            })
    }
}