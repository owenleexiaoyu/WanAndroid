package cc.lixiaoyu.wanandroid.service;

import java.util.List;

import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WanAndroidService {

    /**
     * 获取首页banner
     * http://www.wanandroid.com/banner/json
     *
     * @return
     */
    @GET("banner/json")
    Call<WanAndroidResult<List<Banner>>> getBanner();

    @GET("article/list/{page}/json")
    Call<WanAndroidResult<ArticlePage>> getArticleList(@Path("page") String page);

}
