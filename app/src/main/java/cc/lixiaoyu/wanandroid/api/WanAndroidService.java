package cc.lixiaoyu.wanandroid.api;

import android.support.annotation.Nullable;

import java.util.List;

import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.entity.Nav;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.entity.ProjectPage;
import cc.lixiaoyu.wanandroid.entity.ProjectTitle;
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
    Call<WanAndroidResult<List<Banner>>> getBannerData();

    /**
     * 获取文章列表
     * http://www.wanandroid.com/article/list/1/json?cid=60
     *
     * @param page   页码
     * @param cid    文章所属的二级分类，为空时获取所有的文章
     * @return
     */
    @GET("article/list/{page}/json")
    Call<WanAndroidResult<ArticlePage>> getArticleList(@Path("page") int page, @Nullable @Query("cid") String cid);

    /**
     * 获取知识体系数据
     * http://www.wanandroid.com/tree/json
     *
     * @return
     */
    @GET("tree/json")
    Call<WanAndroidResult<List<PrimaryClass>>> getKnowledgeTreeData();

    /**
     * 获取项目分类信息
     * http://www.wanandroid.com/project/tree/json
     *
     * @return
     */
    @GET("project/tree/json")
    Call<WanAndroidResult<List<ProjectTitle>>> getProjectsData();

    /**
     * 获取项目分类中的项目列表数据
     * @param page   页码，从1开始
     * @param cid    分类的id
     * @return
     */
    @GET("project/list/{page}/json")
    Call<WanAndroidResult<ProjectPage>> getProjectArticlesByCid(@Path("page") int page, @Query("cid") String cid);

    /**
     * 获取导航数据
     * http://www.wanandroid.com/navi/json
     * @return
     */
    @GET("navi/json")
    Call<WanAndroidResult<List<Nav>>> getNavData();

}
