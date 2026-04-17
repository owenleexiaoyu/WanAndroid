package cc.lixiaoyu.wanandroid.kmp.mine.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import cc.lixiaoyu.wanandroid.kmp.AndroidToastContext
import cc.lixiaoyu.wanandroid.kmp.mine.ui.MineAction
import cc.lixiaoyu.wanandroid.kmp.mine.ui.MineScreen
import cc.lixiaoyu.wanandroid.kmp.mine.ui.MineUser

/**
 * 承载 KMP [MineScreen] 的个人页容器；宿主 Activity 需实现 [MineActionHandler] 以处理跳转。
 */
class MineContainerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        AndroidToastContext.init(requireContext().applicationContext)

        val handler = requireActivity() as? MineActionHandler
            ?: error("${requireActivity().javaClass.simpleName} must implement ${MineActionHandler::class.java.name}")

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    MineScreen(
                        user = MineUser(name = "小小的太太阳", id = "27165"),
                        showTopBar = false,
                        onAction = { action ->
                            handler.onMineAction(
                                fragment = this@MineContainerFragment,
                                action = action,
                            )
                        },
                    )
                }
            }
        }
    }

    companion object {
        fun newInstance(): MineContainerFragment = MineContainerFragment()
    }
}

fun interface MineActionHandler {
    fun onMineAction(fragment: Fragment, action: MineAction)
}

