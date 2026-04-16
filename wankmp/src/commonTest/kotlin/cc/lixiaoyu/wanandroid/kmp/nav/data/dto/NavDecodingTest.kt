package cc.lixiaoyu.wanandroid.kmp.nav.data.dto

import cc.lixiaoyu.wanandroid.kmp.nav.data.remote.createDefaultJson
import kotlin.test.Test
import kotlin.test.assertEquals

class NavDecodingTest {

    @Test
    fun decodesWanNavResponseSample() {
        val json = createDefaultJson()
        val raw = """
            {
              "data": [
                {
                  "articles": [
                    {"id": 1, "title": "GitHub", "link": "https://github.com"}
                  ],
                  "name": "常用网站"
                }
              ],
              "errorCode": 0,
              "errorMsg": ""
            }
        """.trimIndent()

        val parsed = json.decodeFromString(WanNavResponse.serializer(), raw)
        assertEquals(0, parsed.errorCode)
        assertEquals(1, parsed.data?.size)
        assertEquals("常用网站", parsed.data?.first()?.name)
        assertEquals("GitHub", parsed.data?.first()?.items?.first()?.title)
    }

    @Test
    fun roundTripWanNavResponse() {
        val json = createDefaultJson()
        val original = WanNavResponse(
            errorCode = 0,
            errorMsg = "",
            data = listOf(
                Nav(
                    name = "A",
                    items = listOf(NavItem(1, "t", "https://x")),
                ),
            ),
        )
        val str = json.encodeToString(WanNavResponse.serializer(), original)
        val back = json.decodeFromString(WanNavResponse.serializer(), str)
        assertEquals(original, back)
    }
}
