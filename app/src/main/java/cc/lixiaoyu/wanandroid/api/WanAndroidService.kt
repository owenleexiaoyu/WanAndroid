package cc.lixiaoyu.wanandroid.api

import cc.lixiaoyu.wanandroid.core.home.banner.BannerModel
import cc.lixiaoyu.wanandroid.core.knowledgemap.model.KnowledgeNode
import cc.lixiaoyu.wanandroid.core.nav.Nav
import cc.lixiaoyu.wanandroid.core.project.model.Project
import cc.lixiaoyu.wanandroid.core.search.model.HotKey
import cc.lixiaoyu.wanandroid.core.search.model.WebSite
import cc.lixiaoyu.wanandroid.core.todo.model.TodoEntity
import cc.lixiaoyu.wanandroid.core.todo.model.TodoEntity.TodoItem
import cc.lixiaoyu.wanandroid.core.wechat.model.WeChatOfficialAccount
import cc.lixiaoyu.wanandroid.entity.*
import retrofit2.http.*

interface WanAndroidService {
    /**
     * 获取首页banner
     * http://www.wanandroid.com/banner/json
     *
     * @return
     */
    @GET("banner/json")
    suspend fun getBannerDataNew(): WanResponse<List<BannerModel>>

    /**
     * 获取文章列表
     * http://www.wanandroid.com/article/list/1/json?cid=60
     *
     * @param page   页码
     * @param cid    文章所属的二级分类，为空时获取所有的文章
     * @return
     */
    @GET("article/list/{page}/json")
    suspend fun getArticleListNew(
        @Path("page") page: Int,
        @Query("cid") cid: String?
    ): WanResponse<ArticlePageData>

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
    suspend fun getTopArticlesNew(): WanResponse<List<Article>>

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
    suspend fun getNavDataNew(): WanResponse<List<Nav>>

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
        @Field("k") k: String): WanResponse<ArticlePageData>

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
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): WanResponse<User>

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
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") rePassword: String
    ): WanResponse<User>

    /**
     * 用户退出登录
     *
     * http://www.wanandroid.com/user/logout/json
     *
     * @return
     */
    @GET("user/logout/json")
    suspend fun logout(): WanResponse<String>

    /**
     * 获取收藏的文章列表
     *
     * https://www.wanandroid.com/lg/collect/list/0/json
     * @return
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectionArticleList(@Path("page") page: Int): WanResponse<ArticlePageData>

    /**
     * 收藏文章
     *
     * https://www.wanandroid.com/lg/collect/1165/json
     * @param articleId
     * @return
     */
    @POST("lg/collect/{articleId}/json")
    suspend fun collectArticle(@Path("articleId") articleId: Int): WanResponse<String>

    /**
     * 从文章列表中取消收藏文章
     * 使用 articleId，目前收藏页也使用这个接口，因为没有做主动收藏的功能，收藏页里的文章都是站内文章，都有 articleId
     *
     * https://www.wanandroid.com/lg/uncollect/2805/json
     * @param articleId
     * @return
     */
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollectArticleFromArticleList(@Path("id") articleId: Int): WanResponse<String>

    /**
     * 获取公众号列表
     *
     * https://wanandroid.com/wxarticle/chapters/json
     * @return
     */
    @GET("wxarticle/chapters/json")
    suspend fun getWetChatOfficialAccountList(): WanResponse<List<WeChatOfficialAccount>>

    /**
     * 获取某个公众号下的文章
     * @param id
     * @param page
     * @return
     */
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getWechatPublicArticlesByIdNew(
        @Path("id") id: String,
        @Path("page") page: Int
    ): WanResponse<ArticlePageData>

    /**
     * 根据type来获取todo清单
     *
     * https://wanandroid.com/lg/todo/list/0/json
     * @return
     */
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
    suspend fun deleteTodoItem(@Path("id") id: Int): WanResponse<String>
}