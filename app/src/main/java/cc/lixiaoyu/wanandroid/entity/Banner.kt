package cc.lixiaoyu.wanandroid.entity

import cc.lixiaoyu.wanandroid.core.detail.DetailParam
import com.google.gson.annotations.SerializedName

data class Banner(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("imagePath")
    val imagePath: String = ""
) {
    fun toDetailParam(): DetailParam {
        return DetailParam(id, title, url, DetailParam.DetailType.WEBPAGE)
    }
}