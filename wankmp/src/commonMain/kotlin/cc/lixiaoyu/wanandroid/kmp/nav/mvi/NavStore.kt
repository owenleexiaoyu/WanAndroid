package cc.lixiaoyu.wanandroid.kmp.nav.mvi

import cc.lixiaoyu.wanandroid.kmp.nav.domain.NavRepository
import cc.lixiaoyu.wanandroid.kmp.nav.util.decodeHtmlEntities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NavStore(
    private val repository: NavRepository,
    scope: CoroutineScope,
) {
    private val storeScope = CoroutineScope(scope.coroutineContext + SupervisorJob())

    private val _state = MutableStateFlow(NavState(isLoading = true))
    val state: StateFlow<NavState> = _state

    private val effects = Channel<NavEffect>(capacity = Channel.BUFFERED)
    val effect: Flow<NavEffect> = effects.receiveAsFlow()

    fun dispatch(intent: NavIntent) {
        when (intent) {
            NavIntent.Enter -> load()
            NavIntent.Retry -> load()
            is NavIntent.ClickItem -> {
                storeScope.launch {
                    effects.send(
                        NavEffect.OpenUrl(
                            title = intent.item.title.decodeHtmlEntities(),
                            url = intent.item.link,
                        ),
                    )
                }
            }
        }
    }

    private fun load() {
        storeScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            val result = repository.loadNavigation()
            result
                .onSuccess { list ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = null,
                        categories = list,
                    )
                }
                .onFailure { t ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = t.message ?: t.toString(),
                        categories = emptyList(),
                    )
                }
        }
    }
}

