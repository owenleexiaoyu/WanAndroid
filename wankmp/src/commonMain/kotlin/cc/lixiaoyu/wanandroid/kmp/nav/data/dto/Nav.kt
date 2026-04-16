package cc.lixiaoyu.wanandroid.kmp.nav.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * One navigation category, aligned with [cc.lixiaoyu.wanandroid.core.nav.Nav] in app.
 * JSON field for the link list is `articles`; Kotlin property is [items].
 */
@Serializable
data class Nav(
    @SerialName("articles")
    val items: List<NavItem> = emptyList(),
    @SerialName("name")
    val name: String = "",
)

/**
 * Single site link under a category, aligned with [cc.lixiaoyu.wanandroid.core.nav.NavItem] in app.
 */
@Serializable
data class NavItem(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("title")
    val title: String = "",
    @SerialName("link")
    val link: String = "",
)
