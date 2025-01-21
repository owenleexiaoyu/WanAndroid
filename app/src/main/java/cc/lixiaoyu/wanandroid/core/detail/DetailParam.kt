package cc.lixiaoyu.wanandroid.core.detail

import java.io.Serializable

class DetailParam(
    val articleId: Int,
    val title: String,
    val link: String,
    private val detailType: DetailType,
    val isCollected: Boolean = false,
) : Serializable {

    /**
     * 详情页的内容是否可以收藏，只有 ARTICLE 类型的才可以收藏
     * @return
     */
    val isCollectable: Boolean
        get() = detailType == DetailType.ARTICLE

    enum class DetailType {
        ARTICLE, WEBPAGE
    }
}