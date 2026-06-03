package id.editor.newsapp.ui.postlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.editor.newsapp.data.model.Post
import id.editor.newsapp.data.repository.PostRepository
import id.editor.newsapp.data.repository.Result
import kotlinx.coroutines.launch

class PostListViewModel : ViewModel() {
    private val repository = PostRepository()

    private val _postsState = MutableLiveData<Result<Pair<List<Post>, Int>>>()
    val postsState: LiveData<Result<Pair<List<Post>, Int>>> = _postsState

    private var currentPage = 1
    private var totalPages = 1
    private val postList = mutableListOf<Post>()
    private var isLoadingMore = false

    fun loadPosts(categoryId: Int = -1, tagId: Int = -1, reset: Boolean = false) {
        if (reset) {
            currentPage = 1
            postList.clear()
            totalPages = 1
        }
        if (currentPage > totalPages) return // All pages loaded
        if (isLoadingMore) return

        isLoadingMore = true
        _postsState.value = if (postList.isEmpty()) Result.Loading else Result.Success(Pair(postList, totalPages)) // show loading only for first page load
        
        viewModelScope.launch {
            val result = when {
                categoryId != -1 -> repository.getPostsByCategory(categoryId, currentPage)
                tagId != -1 -> repository.getPostsByTag(tagId, currentPage)
                else -> repository.getPosts(currentPage)
            }
            when (result) {
                is Result.Success -> {
                    totalPages = result.data.second
                    postList.addAll(result.data.first)
                    _postsState.value = Result.Success(Pair(postList.toList(), totalPages))
                    currentPage++
                }
                is Result.Error -> {
                    _postsState.value = Result.Error(result.message, result.code)
                }
                else -> {}
            }
            isLoadingMore = false
        }
    }
}
