package cc.lixiaoyu.wanandroid.entity

import com.google.gson.annotations.SerializedName

data class Integral(
        @SerializedName("coinCount") val coinCount: Int = 0,
        @SerializedName("rank") val rank: Int = 0,
        @SerializedName("level") val level: Int = 0,
        @SerializedName("userId") val userId: Int = 0,
        @SerializedName("username") val username: String = ""
)
