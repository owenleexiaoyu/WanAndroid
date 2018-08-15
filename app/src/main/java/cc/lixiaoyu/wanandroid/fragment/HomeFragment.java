package cc.lixiaoyu.wanandroid.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.ZoomIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.service.WanAndroidService;
import cc.lixiaoyu.wanandroid.util.AppConst;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    @BindView(R.id.fhome_banner)
    BannerViewPager mBannerViewPager;
    @BindView(R.id.fhome_zoom_indicator)
    ZoomIndicator mZoomIndicator;
    private List<Banner.BannerData> mBannerDataList;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                PageBean pageBean = new PageBean.Builder<Banner.BannerData>()
                        .setDataObjects(mBannerDataList)
                        .setIndicator(mZoomIndicator)
                        .builder();
                mBannerViewPager.setPageListener(pageBean,
                        R.layout.layout_banner_item, new PageHelperListener() {
                    @Override
                    public void getItemView(View view, Object o) {
                        Banner.BannerData data = (Banner.BannerData) o;
                        ImageView imageView = view.findViewById(R.id.banner_item_img);
                        Glide.with(getActivity())
                                .load(data.getImagepath())
                                .into(imageView);
                    }
                });
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container, false);
        ButterKnife.bind(this, view);
        sendHttpRequest();

        return view;
    }

    private void sendHttpRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConst.WANANDROID_BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WanAndroidService service = retrofit.create(WanAndroidService.class);
        Call<Banner> call = service.getBanner();
        call.enqueue(new Callback<Banner>() {
            @Override
            public void onResponse(Call<Banner> call, Response<Banner> response) {
                Banner banner = response.body();
                if(banner.getErrorcode() == 0){
                    mBannerDataList = banner.getData();
                    Banner.BannerData bannerData = banner.getData().get(0);
                    Log.d(TAG, "onResponse: ------->图片地址:"+bannerData.getImagepath());
                    Log.d(TAG, "onResponse: ------->链接："+bannerData.getUrl());
                    Log.d(TAG, "onResponse: ------->标题："+bannerData.getTitle());
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onFailure(Call<Banner> call, Throwable t) {
                Toast.makeText(getActivity(), "出错了", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
