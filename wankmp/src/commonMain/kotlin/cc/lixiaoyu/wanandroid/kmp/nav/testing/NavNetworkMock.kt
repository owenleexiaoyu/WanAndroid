package cc.lixiaoyu.wanandroid.kmp.nav.testing

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 测试页选择网络场景；实际行为由 [createNavHttpClient] 使用 Ktor [io.ktor.client.engine.mock.MockEngine] 实现，
 * 业务侧 [cc.lixiaoyu.wanandroid.kmp.nav.data.remote.NavRemoteDataSource] 不感知 Mock。
 */
sealed interface NavNetworkMockScenario {
    data object None : NavNetworkMockScenario

    /** 弱网成功：MockEngine 延迟 [delayMillis] 后返回成功 JSON（不访问外网）。 */
    data class WeakNetworkSuccess(val delayMillis: Long = 3_000L) : NavNetworkMockScenario

    /** 弱网失败：MockEngine 延迟 [delayMillis] 后抛出异常（模拟慢请求后仍失败，不访问外网）。 */
    data class WeakNetworkFailure(
        val delayMillis: Long = 3_000L,
        val message: String = "弱网耗时结束后失败（MockEngine）",
    ) : NavNetworkMockScenario

    /** 连接失败：MockEngine 在延迟后抛出异常（不访问外网）。 */
    data class ConnectionFailure(
        val delayMillis: Long = 200L,
        val message: String = "连接失败（MockEngine）",
    ) : NavNetworkMockScenario

    /** API 业务失败：MockEngine 直接返回 `errorCode != 0` 的 JSON 体。 */
    data class ApiBusinessFailure(
        val errorCode: Int = -1,
        val errorMsg: String = "mock api error",
    ) : NavNetworkMockScenario
}

object NavNetworkMock {
    private val _scenario = MutableStateFlow<NavNetworkMockScenario>(NavNetworkMockScenario.None)
    val scenario: StateFlow<NavNetworkMockScenario> = _scenario.asStateFlow()

    fun setScenario(scenario: NavNetworkMockScenario) {
        _scenario.value = scenario
    }
}
