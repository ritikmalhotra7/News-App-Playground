package com.complete.newsreporter.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.complete.newsreporter.database.DefaultRepository
import com.complete.newsreporter.model.Article
import com.complete.newsreporter.model.NewsResponse
import com.complete.newsreporter.modelX.SosResponse
import com.complete.newsreporter.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: DefaultRepository) :
    ViewModel() {
    val breakingNews: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var searchedQuery: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    val numberReponse: MutableLiveData<Resources<SosResponse>> = MutableLiveData()

    var breakingNewsPage = 1
    var searchedPageNumber = 1
    var breakingNewsResponse: NewsResponse? = null
    var searchNewsResponse: NewsResponse? = null

    fun getNumber() = viewModelScope.launch {
        numberReponse.postValue(Resources.Loading())
        val numbers = repository.getNumber()
        numberReponse.postValue(handleSosReponse(numbers))
    }

    private fun handleSosReponse(response: Response<SosResponse>): Resources<SosResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(resultResponse)
            }
        }
        return Resources.Error(response.message())
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resources.Loading())
        val v = repository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(v))
    }

    fun getSearchedNews(searchQuery: String) = viewModelScope.launch {
        searchedQuery.postValue(Resources.Loading())
        val v = repository.getSearchQuery(searchQuery, searchedPageNumber)
        breakingNews.postValue(handleSearchedNewsResponse(v))
    }

    fun getSavedNews() = repository.getAll()
    fun saveNews(article: Article) = viewModelScope.launch {
        repository.upsert(article)
    }

    fun deleteNews(article: Article) = viewModelScope.launch {
        repository.delete(article)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resources<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldList = breakingNewsResponse!!.articles
                    val newList = resultResponse.articles
                    oldList!!.addAll(newList!!)
                }
                return Resources.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resources.Error(response.message())
    }

    private fun handleSearchedNewsResponse(response: Response<NewsResponse>): Resources<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resources.Error(response.message())
    }
}