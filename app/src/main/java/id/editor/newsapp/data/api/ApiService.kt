package id.editor.newsapp.data.api

import id.editor.newsapp.data.model.Category
import id.editor.newsapp.data.model.Page
import id.editor.newsapp.data.model.Post
import id.editor.newsapp.data.model.Tag
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {

    // ── Posts ──────────────────────────────────────────────────────────────────

    @GET("wp/v2/posts")
    suspend fun getPosts(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("_embed") embed: Int = 1,
        @Query("orderby") orderBy: String = "date",
        @Query("order") order: String = "desc",
        @Query("status") status: String = "publish"
    ): Response<List<Post>>

    @GET("wp/v2/posts")
    suspend fun getPostsByCategory(
        @Query("categories") categoryId: Int,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("_embed") embed: Int = 1,
        @Query("status") status: String = "publish"
    ): Response<List<Post>>

    @GET("wp/v2/posts")
    suspend fun getPostsByTag(
        @Query("tags") tagId: Int,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("_embed") embed: Int = 1,
        @Query("status") status: String = "publish"
    ): Response<List<Post>>

    @GET("wp/v2/posts")
    suspend fun searchPosts(
        @Query("search") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("_embed") embed: Int = 1,
        @Query("status") status: String = "publish"
    ): Response<List<Post>>

    @GET("wp/v2/posts/{id}")
    suspend fun getPost(
        @Path("id") id: Int,
        @Query("_embed") embed: Int = 1
    ): Response<Post>

    @GET("wp/v2/posts")
    suspend fun getStickyPosts(
        @Query("sticky") sticky: Boolean = true,
        @Query("per_page") perPage: Int = 5,
        @Query("_embed") embed: Int = 1,
        @Query("status") status: String = "publish"
    ): Response<List<Post>>

    // ── Categories ─────────────────────────────────────────────────────────────

    @GET("wp/v2/categories")
    suspend fun getCategories(
        @Query("per_page") perPage: Int = 100,
        @Query("orderby") orderBy: String = "count",
        @Query("order") order: String = "desc",
        @Query("hide_empty") hideEmpty: Boolean = true
    ): Response<List<Category>>

    @GET("wp/v2/categories/{id}")
    suspend fun getCategory(
        @Path("id") id: Int
    ): Response<Category>

    // ── Tags ───────────────────────────────────────────────────────────────────

    @GET("wp/v2/tags")
    suspend fun getTags(
        @Query("per_page") perPage: Int = 100,
        @Query("orderby") orderBy: String = "count",
        @Query("order") order: String = "desc",
        @Query("hide_empty") hideEmpty: Boolean = true
    ): Response<List<Tag>>

    @GET("wp/v2/tags/{id}")
    suspend fun getTag(
        @Path("id") id: Int
    ): Response<Tag>

    // ── Pages ──────────────────────────────────────────────────────────────────

    @GET("wp/v2/pages")
    suspend fun getPages(
        @Query("per_page") perPage: Int = 50,
        @Query("orderby") orderBy: String = "menu_order",
        @Query("order") order: String = "asc",
        @Query("_embed") embed: Int = 1,
        @Query("status") status: String = "publish"
    ): Response<List<Page>>

    @GET("wp/v2/pages/{id}")
    suspend fun getPage(
        @Path("id") id: Int,
        @Query("_embed") embed: Int = 1
    ): Response<Page>
}
