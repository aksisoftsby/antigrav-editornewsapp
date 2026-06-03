package id.editor.newsapp.ui.page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.editor.newsapp.data.model.Page
import id.editor.newsapp.data.repository.PageRepository
import id.editor.newsapp.data.repository.Result
import kotlinx.coroutines.launch

class PageViewModel : ViewModel() {
    private val repository = PageRepository()

    private val _pages = MutableLiveData<Result<List<Page>>>()
    val pages: LiveData<Result<List<Page>>> = _pages

    private val _pageDetails = MutableLiveData<Result<Page>>()
    val pageDetails: LiveData<Result<Page>> = _pageDetails

    fun fetchPages() {
        _pages.value = Result.Loading
        viewModelScope.launch {
            val result = repository.getPages()
            _pages.value = result
        }
    }

    fun fetchPageDetails(id: Int) {
        _pageDetails.value = Result.Loading
        viewModelScope.launch {
            val result = repository.getPage(id)
            _pageDetails.value = result
        }
    }
}
