package cc.lixiaoyu.wanandroid.service;

import cc.lixiaoyu.wanandroid.entity.Banner;
import retrofit2.Call;
import retrofit2.http.GET;

public interface WanAndroidService {

    /**
     * 获取首页banner
     * @return
     */
    @GET("banner/json")
    Call<Banner> getBanner();
}
