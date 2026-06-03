package id.editor.newsapp.data.repository

import id.editor.newsapp.data.api.RetrofitClient
import id.editor.newsapp.data.model.Tag

class TagRepository {
    private val api = RetrofitClient.apiService

    suspend fun getTags(): Result<List<Tag>> {
        return try {
            val response = api.getTags()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error("Failed to load tags: ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getTag(id: Int): Result<Tag> {
        return try {
            val response = api.getTag(id)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Failed to load tag: ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
}
