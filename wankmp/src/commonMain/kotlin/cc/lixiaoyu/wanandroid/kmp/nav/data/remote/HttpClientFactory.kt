package cc.lixiaoyu.wanandroid.kmp.nav.data.remote

import io.ktor.client.HttpClient

/**
 * Platform [HttpClient] with JSON content negotiation (OkHttp on Android, Darwin on iOS).
 */
expect fun createNavigationHttpClient(): HttpClient
