package cc.lixiaoyu.wanandroid.api;

import android.support.annotation.Nullable;

import java.util.List;

import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WanAndroidService {

    /**
     * 获取首页banner
     * http://www.wanandroid.com/banner/json
     *
     * @return
     */
    @GET("banner/json")
    Call<WanAndroidResult<List<Banner>>> getBanner();

    /**
     * 获取文章列表
     * @param page   页码
     * @param cid    文章所属的二级分类，为空时获取所有的文章
     * @return
     */
    @GET("article/list/{page}/json")
    Call<WanAndroidResult<ArticlePage>> getArticleList(@Path("page") String page, @Nullable @Query("cid") String cid);

    @GET("tree/json")
    Call<WanAndroidResult<List<PrimaryClass>>> getSystemData();

}
