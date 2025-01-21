package cc.lixiaoyu.wanandroid.core.project.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Project(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = ""
): Serializable
