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
import cc.lixiaoyu.wanandroid.core.detail.DetailParam
import cc.lixiaoyu.wanandroid.core.detail.DetailViewModel
import cc.lixiaoyu.wanandroid.core.detail.ShareUtils
import cc.lixiaoyu.wanandroid.util.MarginItemDecoration
import cc.lixiaoyu.wanandroid.util.px
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailMoreSheet: BottomSheetDialogFragment() {

    private lateinit var detailViewModel: DetailViewModel

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
        detailViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

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
        return listOf(
            DetailMoreItem(
                title = getString(R.string.collect),
                icon = R.drawable.ic_favorite_full,
                onClick = {
                    dismiss()
                    detailViewModel.collectArticle()
                }
            ),
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
                    detailViewModel.refresh()
                }
            ),
        )
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