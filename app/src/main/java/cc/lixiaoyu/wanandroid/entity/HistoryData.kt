package cc.lixiaoyu.wanandroid.entity

import org.litepal.crud.LitePalSupport

/**
 * 搜索历史的实体类
 */
class HistoryData(var data: String) : LitePalSupport() {
    var id = 0
}