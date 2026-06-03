package id.editor.newsapp.data.repository

import id.editor.newsapp.data.api.RetrofitClient
import id.editor.newsapp.data.model.Category

class CategoryRepository {
    private val api = RetrofitClient.apiService

    suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = api.getCategories()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error("Failed to load categories: ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getCategory(id: Int): Result<Category> {
        return try {
            val response = api.getCategory(id)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Failed to load category: ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
}
