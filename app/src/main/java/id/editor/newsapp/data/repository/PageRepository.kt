package id.editor.newsapp.data.repository

import id.editor.newsapp.data.api.RetrofitClient
import id.editor.newsapp.data.model.Page

class PageRepository {
    private val api = RetrofitClient.apiService

    suspend fun getPages(): Result<List<Page>> {
        return try {
            val response = api.getPages()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error("Failed to load pages: ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }

    suspend fun getPage(id: Int): Result<Page> {
        return try {
            val response = api.getPage(id)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Failed to load page: ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
}
