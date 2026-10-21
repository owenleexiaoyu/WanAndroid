package cc.lixiaoyu.wanandroid.kmp.core.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.cinterop.*
import kotlinx.coroutines.suspendCancellableCoroutine
import org.jetbrains.skia.Image as SkiaImage
import platform.Foundation.NSURL
import platform.Foundation.NSURLSession
import platform.Foundation.NSHTTPURLResponse
import platform.Foundation.dataTaskWithURL
import platform.posix.memcpy
import kotlin.coroutines.resume

/** NSURLSession owns transport caching; disposed cells cancel outstanding requests. */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WanAsyncImage(url: String, contentDescription: String?, modifier: Modifier, contentScale: ContentScale) {
    val bitmap by produceState<ImageBitmap?>(null, url) {
        value = null
        val target = NSURL.URLWithString(url) ?: return@produceState
        val bytes = suspendCancellableCoroutine<ByteArray?> { continuation ->
            val task = NSURLSession.sharedSession.dataTaskWithURL(target) { data, response, error ->
                val status = (response as? NSHTTPURLResponse)?.statusCode ?: 0
                val result = if (error == null && data != null && status in 200L..299L && data.length > 0u) {
                    ByteArray(data.length.toInt()).also { array ->
                        array.usePinned { memcpy(it.addressOf(0), data.bytes, data.length) }
                    }
                } else null
                continuation.resume(result)
            }
            continuation.invokeOnCancellation { task.cancel() }
            task.resume()
        }
        value = bytes?.let { runCatching { SkiaImage.makeFromEncoded(it).toComposeImageBitmap() }.getOrNull() }
    }
    bitmap?.let { Image(it, contentDescription, modifier, contentScale = contentScale) }
}
