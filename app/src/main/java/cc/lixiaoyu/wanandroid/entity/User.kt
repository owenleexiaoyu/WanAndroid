package cc.lixiaoyu.wanandroid.entity

import java.io.Serializable

class User: Serializable {
    var chapterTops: List<String>? = null
    var collectIds: List<Int>? = null
    var email: String? = null
    var icon: String? = null
    var id = 0
    var password: String? = null
    var token: String? = null
    var type = 0
    var username: String? = null
}