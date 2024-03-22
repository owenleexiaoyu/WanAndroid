package cc.lixiaoyu.wanandroid.core.wechat.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeChatOfficialAccount(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
): Serializable