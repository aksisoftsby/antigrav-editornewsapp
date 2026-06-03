package id.editor.newsapp.ui.postdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.editor.newsapp.data.model.Post
import id.editor.newsapp.data.repository.PostRepository
import id.editor.newsapp.data.repository.Result
import kotlinx.coroutines.launch

class PostDetailViewModel : ViewModel() {
    private val repository = PostRepository()

    private val _post = MutableLiveData<Result<Post>>()
    val post: LiveData<Result<Post>> = _post

    fun loadPost(id: Int) {
        _post.value = Result.Loading
        viewModelScope.launch {
            val result = repository.getPost(id)
            _post.value = result
        }
    }
}
