package cc.lixiaoyu.wanandroid.util

import android.content.Context
import android.widget.ImageView
import cc.lixiaoyu.wanandroid.entity.Banner
import com.bumptech.glide.Glide
import com.youth.banner.loader.ImageLoader

class GlideImageLoader : ImageLoader() {
    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        val banner = path as? Banner ?: return
        Glide.with(context).load(banner.imagePath).into(imageView)
    }
}