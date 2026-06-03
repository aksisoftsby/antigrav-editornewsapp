package id.editor.newsapp.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.editor.newsapp.data.model.Category
import id.editor.newsapp.data.repository.CategoryRepository
import id.editor.newsapp.data.repository.Result
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {
    private val repository = CategoryRepository()

    private val _categories = MutableLiveData<Result<List<Category>>>()
    val categories: LiveData<Result<List<Category>>> = _categories

    init {
        fetchCategories()
    }

    fun fetchCategories() {
        _categories.value = Result.Loading
        viewModelScope.launch {
            val result = repository.getCategories()
            _categories.value = result
        }
    }
}
