package cc.lixiaoyu.wanandroid.core.search.model

import com.google.gson.annotations.SerializedName

data class HotKey(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = ""
)