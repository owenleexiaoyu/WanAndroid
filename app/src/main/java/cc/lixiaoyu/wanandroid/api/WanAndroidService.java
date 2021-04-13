package cc.lixiaoyu.wanandroid.api;

import androidx.annotation.Nullable;

import java.util.List;

import cc.lixiaoyu.wanandroid.entity.Article;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.Banner;
import cc.lixiaoyu.wanandroid.entity.CollectionPage;
import cc.lixiaoyu.wanandroid.entity.HotKey;
import cc.lixiaoyu.wanandroid.entity.Nav;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.entity.ProjectPage;
import cc.lixiaoyu.wanandroid.entity.ProjectTitle;
import cc.lixiaoyu.wanandroid.core.todo.TodoEntity;
import cc.lixiaoyu.wanandroid.entity.User;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.core.search.WebSite;
import cc.lixiaoyu.wanandroid.entity.WechatPage;
import cc.lixiaoyu.wanandroid.entity.WechatTitle;
import io.reactivex.Observable;
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
    Observable<WanAndroidResult<List<Banner>>> getBannerData();

    /**
     * 获取文章列表
     * http://www.wanandroid.com/article/list/1/json?cid=60
     *
     * @param page   页码
     * @param cid    文章所属的二级分类，为空时获取所有的文章
     * @return
     */
    @GET("article/list/{page}/json")
    Observable<WanAndroidResult<ArticlePage>> getArticleList(@Path("page") int page, @Nullable @Query("cid") String cid);

    /**
     * 获取置顶文章
     * https://www.wanandroid.com/article/top/json
     *
     * @return
     */
    @GET("article/top/json")
    Observable<WanAndroidResult<List<Article>>> getTopArticles();

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
    Observable<WanAndroidResult<ProjectPage>> getProjectArticlesByCid(@Path("page") int page, @Query("cid") String cid);

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

    /**
     * 获取收藏的文章列表
     *
     * https://www.wanandroid.com/lg/collect/list/0/json
     * @return
     */
    @GET("lg/collect/list/{page}/json")
    Observable<WanAndroidResult<CollectionPage>> getCollectionArticleList(@Path("page") int page);

    /**
     * 收藏文章
     *
     * https://www.wanandroid.com/lg/collect/1165/json
     * @param articleId
     * @return
     */
    @POST("lg/collect/{articleId}/json")
    Observable<WanAndroidResult<String>> collectArticle(@Path("articleId") int articleId);

    /**
     * 从收藏界面中取消收藏文章
     *
     * https://www.wanandroid.com/lg/uncollect/2805/json
     * @param articleId
     * @return
     */
    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    Observable<WanAndroidResult<String>> unCollectArticleFromCollectionPage(@Path("id") int articleId, @Field("originId") int originId);

    /**
     * 从文章列表中取消收藏文章
     *
     * https://www.wanandroid.com/lg/uncollect/2805/json
     * @param articleId
     * @return
     */
    @POST("lg/uncollect_originId/{id}/json")
    Observable<WanAndroidResult<String>> unCollectArticleFromArticleList(@Path("id") int articleId);

    /**
     * 获取公众号列表
     *
     * https://wanandroid.com/wxarticle/chapters/json
     * @return
     */
    @GET("wxarticle/chapters/json")
    Observable<WanAndroidResult<List<WechatTitle>>> getWetchatPublicTitles();

    /**
     * 获取某个公众号下的文章
     * @param id
     * @param page
     * @return
     */
    @GET("wxarticle/list/{id}/{page}/json")
    Observable<WanAndroidResult<WechatPage>> getWechatPublicArticlesById(@Path("id") int id, @Path("page") int page);

    /**
     * 根据type来获取todo清单
     *
     * https://wanandroid.com/lg/todo/list/0/json
     * @return
     */
    @GET("lg/todo/list/{type}/json")
    Observable<WanAndroidResult<TodoEntity>> getTodoListByType(@Path("type") int type);

    /**
     * 新增一个todo
     *
     * https://wanandroid.com/lg/todo/add/json
     *
     * @param title
     * @param content
     * @param date
     * @param type
     * @return
     */
    @POST("lg/todo/add/json")
    @FormUrlEncoded
    Observable<WanAndroidResult<TodoEntity.TodoItem>> addTodoItem(@Field("title") String title,
                                                                  @Field("content") String content,
                                                                  @Field("date") String date,
                                                                  @Field("type") int type);

    /**
     * 更新TodoItem的内容
     * https://www.wanandroid.com/lg/todo/update/83/json
     *
     * @param id
     * @param title
     * @param content
     * @param date
     * @param type
     * @param status  0为未完成，1为完成
     * @return
     */
    @POST("lg/todo/update/{id}/json")
    @FormUrlEncoded
    Observable<WanAndroidResult<TodoEntity.TodoItem>> updateTodoItem(@Path("id") int id,
                                                                     @Field("title") String title,
                                                                     @Field("content") String content,
                                                                     @Field("date") String date,
                                                                     @Field("type") int type,
                                                                     @Field("status") int status);

    /**
     * 删除一条todoitem
     * https://www.wanandroid.com/lg/todo/delete/83/json
     *
     * @param id
     * @return
     */
    @POST("lg/todo/delete/{id}/json")
    Observable<WanAndroidResult<String>> deleteTodoItem(@Path("id") int id);

}
