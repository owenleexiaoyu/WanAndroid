package cc.lixiaoyu.wanandroid.kmp.nav.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Envelope for `navi/json`, same shape as [cc.lixiaoyu.wanandroid.entity.WanResponse] in app.
 */
@Serializable
data class WanNavResponse(
    @SerialName("errorCode")
    val errorCode: Int = 0,
    @SerialName("errorMsg")
    val errorMsg: String = "",
    @SerialName("data")
    val data: List<Nav>? = null,
)
