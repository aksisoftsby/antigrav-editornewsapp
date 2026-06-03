package id.editor.newsapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.editor.newsapp.data.model.Post
import id.editor.newsapp.data.repository.PostRepository
import id.editor.newsapp.data.repository.Result
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = PostRepository()

    private val _searchResults = MutableLiveData<Result<Pair<List<Post>, Int>>>()
    val searchResults: LiveData<Result<Pair<List<Post>, Int>>> = _searchResults

    private var currentQuery = ""
    private var currentPage = 1
    private var totalPages = 1
    private val postList = mutableListOf<Post>()
    private var isLoading = false

    fun searchPosts(query: String, reset: Boolean = false) {
        if (reset || query != currentQuery) {
            currentQuery = query
            currentPage = 1
            postList.clear()
            totalPages = 1
        }
        if (currentQuery.isEmpty()) {
            _searchResults.value = Result.Success(Pair(emptyList(), 1))
            return
        }
        if (currentPage > totalPages) return
        if (isLoading) return

        isLoading = true
        _searchResults.value = if (postList.isEmpty()) Result.Loading else Result.Success(Pair(postList, totalPages))

        viewModelScope.launch {
            val result = repository.searchPosts(currentQuery, currentPage)
            when (result) {
                is Result.Success -> {
                    totalPages = result.data.second
                    postList.addAll(result.data.first)
                    _searchResults.value = Result.Success(Pair(postList.toList(), totalPages))
                    currentPage++
                }
                is Result.Error -> {
                    _searchResults.value = Result.Error(result.message, result.code)
                }
                else -> {}
            }
            isLoading = false
        }
    }
}
