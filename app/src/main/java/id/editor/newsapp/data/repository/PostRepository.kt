package id.editor.newsapp.data.repository

import id.editor.newsapp.data.api.RetrofitClient
import id.editor.newsapp.data.model.Post

class PostRepository {
    private val api = RetrofitClient.apiService

    suspend fun getPosts(page: Int = 1, perPage: Int = 20): Result<Pair<List<Post>, Int>> {
        return try {
            val response = api.getPosts(page = page, perPage = perPage)
            if (response.isSuccessful) {
                val posts = response.body() ?: emptyList()
                val totalPages = response.headers()["X-WP-TotalPages"]?.toIntOrNull() ?: 1
                Result.Success(Pair(posts, totalPages))
            } else {
                Result.Error("Failed to load posts: ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getPostsByCategory(
        categoryId: Int,
        page: Int = 1,
        perPage: Int = 20
    ): Result<Pair<List<Post>, Int>> {
        return try {
            val response = api.getPostsByCategory(categoryId = categoryId, page = page, perPage = perPage)
            if (response.isSuccessful) {
                val posts = response.body() ?: emptyList()
                val totalPages = response.headers()["X-WP-TotalPages"]?.toIntOrNull() ?: 1
                Result.Success(Pair(posts, totalPages))
            } else {
                Result.Error("Failed to load posts: ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getPostsByTag(
        tagId: Int,
        page: Int = 1,
        perPage: Int = 20
    ): Result<Pair<List<Post>, Int>> {
        return try {
            val response = api.getPostsByTag(tagId = tagId, page = page, perPage = perPage)
            if (response.isSuccessful) {
                val posts = response.body() ?: emptyList()
                val totalPages = response.headers()["X-WP-TotalPages"]?.toIntOrNull() ?: 1
                Result.Success(Pair(posts, totalPages))
            } else {
                Result.Error("Failed to load posts: ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun searchPosts(
        query: String,
        page: Int = 1,
        perPage: Int = 20
    ): Result<Pair<List<Post>, Int>> {
        return try {
            val response = api.searchPosts(query = query, page = page, perPage = perPage)
            if (response.isSuccessful) {
                val posts = response.body() ?: emptyList()
                val totalPages = response.headers()["X-WP-TotalPages"]?.toIntOrNull() ?: 1
                Result.Success(Pair(posts, totalPages))
            } else {
                Result.Error("Failed to search: ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getPost(id: Int): Result<Post> {
        return try {
            val response = api.getPost(id)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Failed to load post: ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getLatestPosts(perPage: Int = 5): Result<List<Post>> {
        return try {
            val response = api.getPosts(page = 1, perPage = perPage)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error("Failed to load posts: ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
}
