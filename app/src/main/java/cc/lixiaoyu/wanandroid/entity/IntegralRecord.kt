package cc.lixiaoyu.wanandroid.entity

/**
 * 积分获取的实体类
 */
data class IntegralRecord(
        val coinCount: Int = 0,
        val date: Long = 0L,
        val desc: String = "",
        val id: Long = 0L,
        val reason: String = "",
        val type: Int = 0,
        val userId: Int = 0,
        val userName: String = ""
)