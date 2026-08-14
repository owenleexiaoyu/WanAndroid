package cc.lixiaoyu.wanandroid.kmp.discover.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WanDiscoverResponse<T>(
    @SerialName("data")
    val data: T? = null,
    @SerialName("errorCode")
    val errorCode: Int = 0,
    @SerialName("errorMsg")
    val errorMsg: String = "",
)

@Serializable
data class DiscoverBannerDto(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("title")
    val title: String = "",
    @SerialName("url")
    val url: String = "",
    @SerialName("imagePath")
    val imagePath: String = "",
)

@Serializable
data class PopularWendaDto(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("title")
    val title: String = "",
    @SerialName("link")
    val link: String = "",
)

@Serializable
data class PopularRouteDto(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = "",
)

@Serializable
data class PopularColumnDto(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("url")
    val url: String = "",
)
