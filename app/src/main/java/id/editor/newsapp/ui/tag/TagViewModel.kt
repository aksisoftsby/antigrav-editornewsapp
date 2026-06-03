package id.editor.newsapp.ui.tag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.editor.newsapp.data.model.Tag
import id.editor.newsapp.data.repository.TagRepository
import id.editor.newsapp.data.repository.Result
import kotlinx.coroutines.launch

class TagViewModel : ViewModel() {
    private val repository = TagRepository()

    private val _tags = MutableLiveData<Result<List<Tag>>>()
    val tags: LiveData<Result<List<Tag>>> = _tags

    init {
        fetchTags()
    }

    fun fetchTags() {
        _tags.value = Result.Loading
        viewModelScope.launch {
            val result = repository.getTags()
            _tags.value = result
        }
    }
}
