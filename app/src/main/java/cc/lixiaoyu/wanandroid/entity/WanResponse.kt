package cc.lixiaoyu.wanandroid.entity

import cc.lixiaoyu.wanandroid.util.network.APIException
import com.google.gson.annotations.SerializedName

class WanResponse<T> {

    @SerializedName("errorCode")
    var errorCode: Int = 0

    @SerializedName("errorMsg")
    var errorMsg: String = ""

    @SerializedName("data")
    var data: T? = null
        @Throws(APIException::class)
        get() {
            if (isSuccess()) {
                return field
            } else {
                throw APIException(errorCode, errorMsg)
            }
        }

    fun isSuccess(): Boolean = errorCode == 0
}