package com.complete.newsreporter.database

import androidx.lifecycle.LiveData
import com.complete.newsreporter.model.Article
import com.complete.newsreporter.model.NewsResponse
import com.complete.newsreporter.modelX.SosResponse
import retrofit2.Response

interface NewsRepository {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse>
    suspend fun getSearchQuery(searchedQuery: String, pageNumber: Int): Response<NewsResponse>

    suspend fun upsert(article: Article)
    suspend fun delete(article: Article)
    fun getAll(): LiveData<List<Article>>

    suspend fun getNumber(): Response<SosResponse>
}