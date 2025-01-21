package cc.lixiaoyu.wanandroid.core.home.banner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.core.detail.ArticleDetailActivity
import com.bumptech.glide.Glide
import com.youth.banner.adapter.BannerAdapter

class HomeBannerAdapter(dataList: List<BannerModel>): BannerAdapter<BannerModel, HomeBannerAdapter.HomeBannerViewHolder>(dataList) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): HomeBannerViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_home_banner, parent, false)
        return HomeBannerViewHolder(view)
    }

    override fun onBindView(
        holder: HomeBannerViewHolder?,
        data: BannerModel?,
        position: Int,
        size: Int
    ) {
        holder?.bind(data!!)
    }

    class HomeBannerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val bannerImage: ImageView
        private val bannerText: TextView
        private var banner: BannerModel? = null

        init {
            bannerImage = itemView.findViewById(R.id.banner_image)
            bannerText = itemView.findViewById(R.id.banner_text)
            itemView.setOnClickListener {
                banner?.let { banner ->
                    // Handle click event
                    ArticleDetailActivity.actionStart(it.context, banner.toDetailParam())
                }
            }
        }

        fun bind(data: BannerModel) {
            banner = data
            // Load image
            Glide.with(itemView.context)
                .load(data.imagePath).into(bannerImage)
            // Load text
            bannerText.text = data.title
        }
    }
}