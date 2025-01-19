package cc.lixiaoyu.wanandroid.core.home.banner

import cc.lixiaoyu.wanandroid.core.detail.DetailParam
import com.google.gson.annotations.SerializedName

data class BannerModel(
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