package id.editor.newsapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.editor.newsapp.data.model.Post
import id.editor.newsapp.data.repository.PostRepository
import id.editor.newsapp.data.repository.Result
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = PostRepository()

    private val _posts = MutableLiveData<Result<List<Post>>>()
    val posts: LiveData<Result<List<Post>>> = _posts

    init {
        fetchLatestPosts()
    }

    fun fetchLatestPosts() {
        _posts.value = Result.Loading
        viewModelScope.launch {
            val result = repository.getLatestPosts(perPage = 20)
            _posts.value = result
        }
    }
}
