package com.olibra.news.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.olibra.news.data.local.NewsDatabase.Companion.DATABASE_VERSION
import com.olibra.news.data.local.dao.ArticleDao
import com.olibra.news.data.local.dao.ArticleReadDao
import com.olibra.news.data.local.model.ArticleEntity
import com.olibra.news.data.local.model.ArticleReadEntity

@Database(
    entities = [ArticleEntity::class, ArticleReadEntity::class],
    version = DATABASE_VERSION
)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun articleReadDao(): ArticleReadDao

    companion object {
        const val DATABASE_VERSION = 1
    }
}