package cc.lixiaoyu.wanandroid.kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform