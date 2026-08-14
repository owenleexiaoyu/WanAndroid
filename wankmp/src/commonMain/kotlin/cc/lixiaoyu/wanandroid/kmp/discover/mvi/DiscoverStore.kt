package cc.lixiaoyu.wanandroid.kmp.discover.mvi

import cc.lixiaoyu.wanandroid.kmp.discover.domain.DiscoverRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DiscoverStore(
    private val repository: DiscoverRepository,
    scope: CoroutineScope,
) {
    private val storeScope = CoroutineScope(scope.coroutineContext + SupervisorJob())

    private val _state = MutableStateFlow(DiscoverState())
    val state: StateFlow<DiscoverState> = _state

    private val effects = Channel<DiscoverEffect>(capacity = Channel.BUFFERED)
    val effect: Flow<DiscoverEffect> = effects.receiveAsFlow()

    fun dispatch(intent: DiscoverIntent) {
        when (intent) {
            DiscoverIntent.Enter -> load()
            DiscoverIntent.Retry -> load()
            is DiscoverIntent.BannerPageChanged -> {
                _state.value = _state.value.copy(currentBannerIndex = intent.index)
            }
            is DiscoverIntent.ClickBanner -> openUrl(intent.banner.title, intent.banner.link)
            is DiscoverIntent.ClickPopularItem -> openUrl(intent.item.title, intent.item.link)
            is DiscoverIntent.ClickMore -> {
                storeScope.launch {
                    effects.send(DiscoverEffect.ShowToast("完整列表页暂未实现"))
                }
            }
        }
    }

    private fun load() {
        storeScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            repository.loadDiscover()
                .onSuccess { content ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = null,
                        banners = content.banners,
                        currentBannerIndex = 0,
                        sections = content.sections,
                    )
                }
                .onFailure { t ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = t.message ?: t.toString(),
                        banners = emptyList(),
                        currentBannerIndex = 0,
                        sections = emptyList(),
                    )
                }
        }
    }

    private fun openUrl(title: String, url: String) {
        storeScope.launch {
            if (url.isBlank()) {
                effects.send(DiscoverEffect.ShowToast("链接为空"))
            } else {
                effects.send(DiscoverEffect.OpenUrl(title = title, url = url))
            }
        }
    }
}
