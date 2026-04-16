package cc.lixiaoyu.wanandroid.kmp.nav.util

/**
 * 将常见 HTML 实体转为可展示文本（如 `&amp;` → `&`），用于服务端在 JSON 中返回转义标题的场景。
 * 支持命名实体、十进制 `&#...;`、十六进制 `&#x...;`（大小写不敏感）。
 * 无法识别的 `&...;` 片段会原样保留。
 */
fun String.decodeHtmlEntities(): String {
    if (!contains('&')) return this
    val named = mapOf(
        "amp" to "&",
        "lt" to "<",
        "gt" to ">",
        "quot" to "\"",
        "apos" to "'",
        "nbsp" to "\u00A0",
    )
    val out = StringBuilder(length)
    var i = 0
    while (i < length) {
        if (this[i] != '&') {
            out.append(this[i])
            i++
            continue
        }
        val semi = indexOf(';', startIndex = i + 1)
        if (semi == -1 || semi - i > 32) {
            out.append('&')
            i++
            continue
        }
        val inner = substring(i + 1, semi)
        val replacement = when {
            named.containsKey(inner) -> named[inner]!!
            inner.startsWith("#") -> decodeNumericHtmlEntity(inner)
            else -> null
        }
        if (replacement != null) {
            out.append(replacement)
            i = semi + 1
        } else {
            out.append(substring(i, semi + 1))
            i = semi + 1
        }
    }
    return out.toString()
}

private fun decodeNumericHtmlEntity(inner: String): String? {
    val body = inner.removePrefix("#")
    if (body.isEmpty()) return null
    val code = when {
        body.startsWith("x", ignoreCase = true) -> body.drop(1).toIntOrNull(16)
        else -> body.toIntOrNull(10)
    } ?: return null
    if (code !in 0..0x10FFFF) return null
    if (code in 0xD800..0xDFFF) return null
    return codePointToString(code)
}

private fun codePointToString(code: Int): String =
    when {
        code <= 0xFFFF -> code.toChar().toString()
        code in 0x10000..0x10FFFF -> {
            val u = code - 0x10000
            val high = (0xD800 or (u shr 10)).toChar()
            val low = (0xDC00 or (u and 0x3FF)).toChar()
            "$high$low"
        }
        else -> "\uFFFD"
    }
