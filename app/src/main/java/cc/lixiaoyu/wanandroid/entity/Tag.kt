package cc.lixiaoyu.wanandroid.entity

import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("url")
    val url: String = ""
)