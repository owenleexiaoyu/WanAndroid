package cc.lixiaoyu.wanandroid.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

import cc.lixiaoyu.wanandroid.entity.Banner;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Banner banner = (Banner) path;
        Glide.with(context).load(banner.getImagePath()).into(imageView);
    }
}
