package cc.lixiaoyu.wanandroid.core.detail.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.app.WanApplication
import cc.lixiaoyu.wanandroid.core.collection.CollectAbility
import cc.lixiaoyu.wanandroid.core.detail.DetailParam
import cc.lixiaoyu.wanandroid.core.detail.ReloadArticleDetailEvent
import cc.lixiaoyu.wanandroid.core.detail.ShareUtils
import cc.lixiaoyu.wanandroid.util.AppConst
import cc.lixiaoyu.wanandroid.util.MarginItemDecoration
import cc.lixiaoyu.wanandroid.util.RxBus
import cc.lixiaoyu.wanandroid.util.ToastUtil
import cc.lixiaoyu.wanandroid.util.px
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailMoreSheet: BottomSheetDialogFragment() {

    private lateinit var horizontalList: RecyclerView
    private lateinit var btnCancel: Button

    private var detailParam: DetailParam? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailParam = arguments?.getSerializable(DETAIL_MORE_PARAM_KEY) as? DetailParam
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_detail_more_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        horizontalList = view.findViewById(R.id.detail_more_horizontal_list)
        val adapter = DetailMoreAdapter(buildDetailMoreItemList())
        horizontalList.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
            this.addItemDecoration(MarginItemDecoration(spaceInPx = 10.px, orientation = RecyclerView.HORIZONTAL))
        }
        btnCancel = view.findViewById(R.id.detail_more_cancel)
        btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun buildDetailMoreItemList(): List<DetailMoreItem> {
        val list = mutableListOf<DetailMoreItem>()
        tryAddCollectItem(list)
        return list + listOf(
            DetailMoreItem(
                title = getString(R.string.open_in_browser),
                icon = R.drawable.ic_browser_24dp,
                onClick = {
                    if (detailParam?.link.isNullOrEmpty() || activity == null) {
                        return@DetailMoreItem
                    }
                    ShareUtils.openInBrowser(requireActivity(), requireNotNull(detailParam?.link))
                    dismiss()
                }
            ),
            DetailMoreItem(
                title = getString(R.string.share),
                icon = R.drawable.ic_share,
                onClick = {
                    if (detailParam == null || activity == null) {
                        return@DetailMoreItem
                    }
                    val activity = requireActivity()
                    val shareText = "${activity.getString(R.string.share_hint)}${detailParam!!.title}（${detailParam!!.link}）"
                    ShareUtils.systemShare(activity, shareText)
                    dismiss()
                }
            ),
            DetailMoreItem(
                title = getString(R.string.refresh),
                icon = R.drawable.ic_refresh_32dp,
                onClick = {
                    dismiss()
                    RxBus.getInstance().post(ReloadArticleDetailEvent())
                }
            ),
        )
    }

    private fun tryAddCollectItem(list: MutableList<DetailMoreItem>) {
        if (detailParam?.articleId != null && detailParam?.isCollectable == true) {
            val title = if (detailParam?.isCollected == true) getString(R.string.uncollect)
                else getString(R.string.collect)
            val icon = if (detailParam?.isCollected == true) R.drawable.ic_favorite_border else
                R.drawable.ic_favorite_full
            list.add(
                DetailMoreItem(
                    title = title,
                    icon = icon,
                    onClick = {
                        dismiss()
                        if (detailParam?.isCollected == true) {
                            CollectAbility.unCollectArticle(requireContext(), detailParam!!.articleId) { success ->
                                val toastText = if (success) WanApplication.globalContext?.getString(R.string.uncollect_success)
                                    else WanApplication.globalContext?.getString(R.string.uncollect_fail) ?: return@unCollectArticle
                                ToastUtil.showToast(toastText)
                            }
                        } else {
                            CollectAbility.collectArticle(requireContext(), detailParam!!.articleId) { success ->
                                val toastText = if (success) WanApplication.globalContext?.getString(R.string.collect_success)
                                    else WanApplication.globalContext?.getString(R.string.collect_fail) ?: return@collectArticle
                                ToastUtil.showToast(toastText)
                            }
                        }
                    }
                )
            )
        }
    }

    companion object {
        private const val DETAIL_MORE_PARAM_KEY = "detail_more_param_key"

        fun newInstance(detailParam: DetailParam): DetailMoreSheet {
            val bundle = Bundle()
            bundle.putSerializable(DETAIL_MORE_PARAM_KEY, detailParam)
            val sheet = DetailMoreSheet()
            sheet.arguments = bundle
            return sheet
        }
    }
}