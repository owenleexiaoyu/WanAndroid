package cc.lixiaoyu.wanandroid.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.lang.Exception

typealias Block<T> = suspend () -> T
typealias Error = suspend (e: Exception) -> Unit
typealias Cancel = suspend (e: Exception) -> Unit

open class BaseViewModel: ViewModel() {


    fun launch(
            block: Block<Unit>,
            error: Error? = null,
            cancel: Cancel? = null,
            showErrorToast: Boolean = true
    ): Job {
        return viewModelScope.launch {
            try {
                block.invoke()
            } catch (e: Exception) {
                when(e) {
                    is CancellationException -> {
                        cancel?.invoke(e)
                    }
                    else -> {
//                        onError(e, showErrorToast)
                        error?.invoke(e)
                    }
                }
            }
        }
    }

    fun <T> async(block: Block<T>): Deferred<T> {
        return viewModelScope.async {
            block.invoke()
        }
    }

}