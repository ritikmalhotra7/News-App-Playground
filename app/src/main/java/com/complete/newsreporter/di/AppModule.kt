package com.complete.newsreporter.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.complete.newsreporter.api.NewsApi
import com.complete.newsreporter.database.ArticleDao
import com.complete.newsreporter.database.ArticleDatabase
import com.complete.newsreporter.database.DefaultRepository
import com.complete.newsreporter.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideApi() : NewsApi = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
        .build().create(NewsApi::class.java)

    @Singleton
    @Provides
    fun provideDao(db:ArticleDatabase):ArticleDao = db.getArticleDao()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext ctx: Context) = Room.databaseBuilder(ctx,ArticleDatabase::class.java,"article.db").build()

    @Singleton
    @Provides
    fun provideRepo(db:ArticleDatabase,api:NewsApi) = DefaultRepository(db,api)
}