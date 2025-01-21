/**
 * Copyright 2021 json.cn
 */
package cc.lixiaoyu.wanandroid.entity

import com.google.gson.annotations.SerializedName

/**
 * 分页加载的数据
 */
open class PageData<T> {
    @SerializedName("curPage")
    val curPage = 0

    @SerializedName("datas")
    val dataList: List<T> = emptyList()

    @SerializedName("offset")
    val offset = 0

    @SerializedName("over")
    val over = false

    @SerializedName("pageCount")
    val pageCount = 0

    @SerializedName("size")
    val size = 0

    @SerializedName("total")
    val total = 0
}