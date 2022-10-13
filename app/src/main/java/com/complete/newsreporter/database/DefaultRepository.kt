package com.complete.newsreporter.database

import com.complete.newsreporter.api.NewsApi
import com.complete.newsreporter.model.Article
import javax.inject.Inject


class DefaultRepository @Inject constructor(private val db :ArticleDatabase, private val api: NewsApi):NewsRepository{

    override suspend fun getBreakingNews(countryCode:String, pageNumber:Int) = api.getBreakingNews(countryCode,pageNumber)
    override suspend fun getSearchQuery(searchedQuery:String, pageNumber: Int) = api.searchForNews(searchedQuery,pageNumber)

    override suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)
    override suspend fun delete(article: Article) = db.getArticleDao().delete(article)
    override fun getAll() = db.getArticleDao().getAllArticles()

    override suspend fun getNumber() = api.getNumber()
}