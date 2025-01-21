package cc.lixiaoyu.wanandroid.entity

import cc.lixiaoyu.wanandroid.core.detail.DetailParam
import java.io.Serializable

class Article : Serializable {
    var author: String = ""
    var chapterName: String = ""
    var isCollect: Boolean = false
    var desc: String = ""
    var envelopePic: String = ""
    var id: Int = 0
    var link: String = ""
    var niceDate: String = ""
    var title: String = ""
    var type: Int = 0   // 0 非置顶，1 置顶
    var shareUser: String = ""

    fun toDetailParam(): DetailParam {
        return DetailParam(id, title!!, link!!, DetailParam.DetailType.ARTICLE, isCollect)
    }
}