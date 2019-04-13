package cc.lixiaoyu.wanandroid.api;

import android.support.annotation.Nullable;

import java.util.List;

import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.entity.HotKey;
import cc.lixiaoyu.wanandroid.entity.Nav;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.entity.ProjectPage;
import cc.lixiaoyu.wanandroid.entity.ProjectTitle;
import cc.lixiaoyu.wanandroid.entity.User;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.entity.WebSite;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    Observable<WanAndroidResult<List<PrimaryClass>>> getKnowledgeTreeData();

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
    Observable<WanAndroidResult<List<Nav>>> getNavData();

    /**
     * 获取搜索热词
     * http://www.wanandroid.com//hotkey/json
     *
     * @return
     */
    @GET("hotkey/json")
    Observable<WanAndroidResult<List<HotKey>>> getHotKey();

    /**
     * 获取常用网站
     * http://www.wanandroid.com/friend/json
     *
     * @return
     */
    @GET("friend/json")
    Observable<WanAndroidResult<List<WebSite>>> getCommonSite();

    /**
     * 通过关键词进行搜索
     * https://www.wanandroid.com/article/query/0/json
     *
     * @param page
     * @param k
     * @return
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    Observable<WanAndroidResult<ArticlePage>> searchArticle(@Path("page") int page, @Field("k") String k);


    /**
     * 登录
     * @param username  用户名
     * @param password  密码
     * http://www.wanandroid.com/user/login
     *
     * @return
     */
    @POST("user/login")
    @FormUrlEncoded
    Call<WanAndroidResult<User>> login(@Field("username") String username, @Field("password") String password);

    /**
     * 注册
     * @param username      用户名
     * @param password      密码
     * @param rePassword    再次输入密码
     * http://www.wanandroid.com/user/register
     *
     * @return
     */
    @POST("user/register")
    @FormUrlEncoded
    Call<WanAndroidResult<User>> register(@Field("username") String username,
                                          @Field("password") String password,
                                          @Field("repassword") String rePassword);

    /**
     * 退出登录
     *
     * http://www.wanandroid.com/user/logout/json
     *
     * @return
     */
    @GET("user/logout/json")
    Observable<WanAndroidResult<String>> logout();

}
