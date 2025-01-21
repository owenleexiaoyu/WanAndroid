package cc.lixiaoyu.wanandroid.core.search.model

import cc.lixiaoyu.wanandroid.core.detail.DetailParam
import com.google.gson.annotations.SerializedName

data class WebSite(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("link") val link: String,
) {
    fun toDetailParam(): DetailParam {
        return DetailParam(id, name, link, DetailParam.DetailType.WEBPAGE, false)
    }
}