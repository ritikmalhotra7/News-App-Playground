package com.complete.newsreporter.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.complete.newsreporter.model.Article

@Database(entities = [Article::class],version = 1)
@TypeConverters(Converters::class)
abstract class ArticleDatabase:RoomDatabase() {
    abstract fun getArticleDao(): ArticleDao
}