package cc.lixiaoyu.wanandroid.core.nav

import cc.lixiaoyu.wanandroid.core.detail.DetailParam
import com.google.gson.annotations.SerializedName

data class Nav(
    @SerializedName("articles")
    val items: List<NavItem> = emptyList(),
    @SerializedName("name")
    val name: String = ""
)

data class NavItem(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("link")
    val link: String = "",
) {
    fun toDetailParam(): DetailParam {
        return DetailParam(id, title, link, DetailParam.DetailType.WEBPAGE)
    }
}