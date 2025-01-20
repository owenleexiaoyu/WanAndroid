package cc.lixiaoyu.wanandroid.core.account

import android.annotation.SuppressLint
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import cc.lixiaoyu.wanandroid.entity.User
import cc.lixiaoyu.wanandroid.util.RxBus
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager
import cc.lixiaoyu.wanandroid.util.storage.DataManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object AccountManager {

    private val service = RetrofitManager.wanAndroidService
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _userLiveData: MutableLiveData<User?> = MutableLiveData()
    var isLoginLiveData: LiveData<Boolean> = _userLiveData.map { it != null }

    init {
        loadUserFromSp()
    }

    // 从 SP 中读取 User 信息
    private fun loadUserFromSp() {
        val loginAccount = DataManager.loginAccount
        val user = if (!loginAccount.isNullOrEmpty()) {
            User().apply {
                username = loginAccount
                password = DataManager.loginPassword
            }
        } else {
            null
        }
        _userLiveData.value = user
    }

    private fun updateUser(user: User?) {
        DataManager.loginAccount = user?.username ?: ""
        if (Looper.getMainLooper() == Looper.myLooper()) {
            _userLiveData.value = user
        } else {
            _userLiveData.postValue(user)
        }
    }

    fun isLogin(): Boolean = isLoginLiveData.value ?: false

    @SuppressLint("CheckResult")
    fun signUp(
        userName: String,
        password: String,
        passwordConfirm: String,
        callback: LoginCallback? = null
    ) {
        coroutineScope.launch {
            try {
                val me = service.register(userName, password, passwordConfirm).data ?: return@launch
                updateUser(me)
                callback?.onSuccess(me)
                RxBus.instance.post(LoginEvent(true))
            } catch (t: Throwable) {
                callback?.onFail(t)
                t.printStackTrace()
            }
        }
    }

    @SuppressLint("CheckResult")
    fun signIn(
        userName: String,
        password: String,
        callback: LoginCallback? = null
    ) {
        coroutineScope.launch {
            try {
                val me = service.login(userName, password).data ?: return@launch
                updateUser(me)
                callback?.onSuccess(me)
                RxBus.instance.post(LoginEvent(true))
            } catch (t: Throwable) {
                callback?.onFail(t)
                t.printStackTrace()
            }
        }
    }

    @SuppressLint("CheckResult")
    fun logout(callback: LogoutCallback? = null) {
        coroutineScope.launch {
            try {
                val result = service.logout().data
                updateUser(null)
                callback?.onSuccess()
                RxBus.instance.post(LoginEvent(false))
            } catch (t: Throwable) {
                callback?.onFail(t)
                t.printStackTrace()
            }
        }
    }
}