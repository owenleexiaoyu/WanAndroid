package cc.lixiaoyu.wanandroid.api

import cc.lixiaoyu.wanandroid.core.knowledgemap.model.KnowledgeNode
import cc.lixiaoyu.wanandroid.core.nav.Nav
import cc.lixiaoyu.wanandroid.core.project.model.Project
import cc.lixiaoyu.wanandroid.core.search.model.HotKey
import cc.lixiaoyu.wanandroid.core.search.model.WebSite
import cc.lixiaoyu.wanandroid.core.todo.model.TodoEntity
import cc.lixiaoyu.wanandroid.core.todo.model.TodoEntity.TodoItem
import cc.lixiaoyu.wanandroid.entity.*
import io.reactivex.Observable
import retrofit2.http.*

interface WanAndroidService {
    /**
     * 获取首页banner
     * http://www.wanandroid.com/banner/json
     *
     * @return
     */
    @GET("banner/json")
    fun getBannerData(): Observable<WanAndroidResponse<List<Banner?>?>?>?

    /**
     * 获取文章列表
     * http://www.wanandroid.com/article/list/1/json?cid=60
     *
     * @param page   页码
     * @param cid    文章所属的二级分类，为空时获取所有的文章
     * @return
     */
    @GET("article/list/{page}/json")
    fun getArticleList(@Path("page") page: Int,
                       @Query("cid") cid: String?): Observable<WanAndroidResponse<ArticlePage?>?>?

    /**
     * 获取文章列表
     * http://www.wanandroid.com/article/list/1/json?cid=60
     *
     * @param page   页码
     * @param cid    文章所属的二级分类，为空时获取所有的文章
     * @return
     */
    @GET("article/list/{page}/json")
    suspend fun getArticleListByCid(
        @Path("page") page: Int,
        @Query("cid") cid: String
    ): WanResponse<ArticlePageData>

    /**
     * 获取置顶文章
     * https://www.wanandroid.com/article/top/json
     *
     * @return
     */
    @GET("article/top/json")
    fun getTopArticles(): Observable<WanAndroidResponse<List<Article?>?>?>?

    /**
     * 获取知识体系数据
     * http://www.wanandroid.com/tree/json
     *
     * @return
     */
    @GET("tree/json")
    suspend fun getKnowledgeMap(): WanResponse<List<KnowledgeNode>>

    /**
     * 获取项目分类信息
     * http://www.wanandroid.com/project/tree/json
     *
     * @return
     */
    @GET("project/tree/json")
    suspend fun getProjectList(): WanResponse<List<Project>>

    /**
     * 获取项目分类中的项目列表数据
     * @param page   页码，从1开始
     * @param cid    分类的id
     * @return
     */
    @GET("project/list/{page}/json")
    suspend fun getProjectArticlesByCidNew(
        @Path("page") page: Int,
        @Query("cid") cid: String?
    ): WanResponse<ArticlePageData>

    /**
     * 获取导航数据
     * http://www.wanandroid.com/navi/json
     * @return
     */
    @GET("navi/json")
    fun getNavData(): Observable<WanAndroidResponse<List<Nav>>>

    /**
     * 获取搜索热词
     * http://www.wanandroid.com//hotkey/json
     *
     * @return
     */
    @GET("hotkey/json")
    suspend fun getHotKeyList(): WanResponse<List<HotKey>>

    /**
     * 获取常用网站
     * http://www.wanandroid.com/friend/json
     *
     * @return
     */
    @GET("friend/json")
    suspend fun getCommonSiteList(): WanResponse<List<WebSite>>
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
    suspend fun searchArticle(
        @Path("page") page: Int,
        @Field("k") k: String): WanResponse<ArticlePage>

    /**
     * 用户登录
     * @param username  用户名
     * @param password  密码
     * http://www.wanandroid.com/user/login
     *
     * @return
     */
    @POST("user/login")
    @FormUrlEncoded
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Observable<WanAndroidResponse<User>>

    /**
     * 用户注册
     * @param username      用户名
     * @param password      密码
     * @param rePassword    再次输入密码
     * http://www.wanandroid.com/user/register
     *
     * @return
     */
    @POST("user/register")
    @FormUrlEncoded
    fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") rePassword: String
    ): Observable<WanAndroidResponse<User>>

    /**
     * 用户退出登录
     *
     * http://www.wanandroid.com/user/logout/json
     *
     * @return
     */
    @GET("user/logout/json")
    fun logout(): Observable<WanAndroidResponse<String>>

    /**
     * 获取收藏的文章列表
     *
     * https://www.wanandroid.com/lg/collect/list/0/json
     * @return
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectionArticleListNew(@Path("page") page: Int): WanResponse<ArticlePageData>

    /**
     * 收藏文章
     *
     * https://www.wanandroid.com/lg/collect/1165/json
     * @param articleId
     * @return
     */
    @POST("lg/collect/{articleId}/json")
    fun collectArticle(@Path("articleId") articleId: Int): Observable<WanAndroidResponse<String>>

    @POST("lg/collect/{articleId}/json")
    suspend fun collectArticleNew(@Path("articleId") articleId: Int): WanResponse<String>

    /**
     * 从收藏界面中取消收藏文章
     *
     * https://www.wanandroid.com/lg/uncollect/2805/json
     * @param articleId
     * @return
     */
    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    fun unCollectArticleFromCollectionPage(@Path("id") articleId: Int,
                                           @Field("originId") originId: Int): Observable<WanAndroidResponse<String?>?>?

    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    suspend fun unCollectArticleFromCollectionPageNew(
        @Path("id") articleId: Int,
        @Field("originId") originId: Int
    ): WanResponse<String>

    /**
     * 从文章列表中取消收藏文章
     *
     * https://www.wanandroid.com/lg/uncollect/2805/json
     * @param articleId
     * @return
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun unCollectArticleFromArticleList(@Path("id") articleId: Int): Observable<WanAndroidResponse<String>>

    /**
     * 获取公众号列表
     *
     * https://wanandroid.com/wxarticle/chapters/json
     * @return
     */
    @GET("wxarticle/chapters/json")
    fun getWetchatPublicTitles(): Observable<WanAndroidResponse<List<WechatTitle?>?>?>?

    /**
     * 获取某个公众号下的文章
     * @param id
     * @param page
     * @return
     */
    @GET("wxarticle/list/{id}/{page}/json")
    fun getWechatPublicArticlesById(@Path("id") id: Int,
                                    @Path("page") page: Int): Observable<WanAndroidResponse<WechatPage?>?>?

    /**
     * 根据type来获取todo清单
     *
     * https://wanandroid.com/lg/todo/list/0/json
     * @return
     */
    @GET("lg/todo/list/{type}/json")
    fun getTodoListByTypeOld(@Path("type") type: Int): Observable<WanAndroidResponse<TodoEntity?>?>?

    @GET("lg/todo/list/{type}/json")
    suspend fun getTodoListByType(@Path("type") type: Int): WanResponse<TodoEntity>

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
    fun addTodoItemOld(@Field("title") title: String?,
                    @Field("content") content: String?,
                    @Field("date") date: String?,
                    @Field("type") type: Int): Observable<WanAndroidResponse<TodoItem?>?>?

    @POST("lg/todo/add/json")
    @FormUrlEncoded
    suspend fun addTodoItem(
        @Field("title") title: String?,
        @Field("content") content: String?,
        @Field("date") date: String?,
        @Field("type") type: Int
    ): WanResponse<TodoItem>

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
    fun updateTodoItemOld(@Path("id") id: Int,
                       @Field("title") title: String?,
                       @Field("content") content: String?,
                       @Field("date") date: String?,
                       @Field("type") type: Int,
                       @Field("status") status: Int): Observable<WanAndroidResponse<TodoItem?>?>?

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
    suspend fun updateTodoItem(
        @Path("id") id: Int,
        @Field("title") title: String?,
        @Field("content") content: String?,
        @Field("date") date: String?,
        @Field("type") type: Int,
        @Field("status") status: Int
    ): WanResponse<TodoItem>

    /**
     * 删除一条todoitem
     * https://www.wanandroid.com/lg/todo/delete/83/json
     *
     * @param id
     * @return
     */
    @POST("lg/todo/delete/{id}/json")
    fun deleteTodoItemOld(@Path("id") id: Int): Observable<WanAndroidResponse<String?>?>?

    /**
     * 删除一条todoitem
     * https://www.wanandroid.com/lg/todo/delete/83/json
     *
     * @param id
     * @return
     */
    @POST("lg/todo/delete/{id}/json")
    suspend fun deleteTodoItem(@Path("id") id: Int): WanResponse<String>
}