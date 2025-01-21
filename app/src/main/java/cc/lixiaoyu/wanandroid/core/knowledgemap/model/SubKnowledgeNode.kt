package cc.lixiaoyu.wanandroid.core.knowledgemap.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SubKnowledgeNode(
    @SerializedName("children")
    val children: List<String>,
    @SerializedName("name")
    val name: String,
    @SerializedName("id")
    val id: Int,
): Serializable
