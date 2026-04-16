package cc.lixiaoyu.wanandroid.kmp.nav.data.remote

import kotlinx.serialization.json.Json

fun createDefaultJson(): Json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
}
