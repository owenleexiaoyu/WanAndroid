package cc.lixiaoyu.wanandroid.core.account

import cc.lixiaoyu.wanandroid.entity.User

/**
 * 注册、登录等操作的回调
 */
interface LoginCallback {
    /**
     * 注册或登录成功，返回 User 数据
     */
    fun onSuccess(user: User)

    /**
     * 注册或登录失败，返回异常
     */
    fun onFail(t: Throwable)
}

interface LogoutCallback {
    /**
     * 登出成功
     */
    fun onSuccess()
    /**
     * 登出失败，返回异常
     */
    fun onFail(t: Throwable)
}