package cc.lixiaoyu.wanandroid.core.knowledgemap.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class KnowledgeNode(
    @SerializedName("children")
    val children: List<SubKnowledgeNode>,
    @SerializedName("name")
    val name: String,
    @SerializedName("id")
    val id: Int,
): Serializable
