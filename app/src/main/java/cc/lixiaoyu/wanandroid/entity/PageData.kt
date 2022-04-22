/**
 * Copyright 2021 json.cn
 */
package cc.lixiaoyu.wanandroid.entity

/**
 * 分页加载的数据
 */
class PageData<T> {
    var curPage = 0
    var datas: List<T> = emptyList()
    var offset = 0
    var over = false
    var pageCount = 0
    var size = 0
    var total = 0
}