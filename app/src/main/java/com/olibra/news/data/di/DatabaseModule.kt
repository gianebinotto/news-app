package com.olibra.news.data.di

import android.content.Context
import androidx.room.Room
import com.olibra.news.data.local.NewsDatabase
import com.olibra.news.data.local.dao.ArticleDao
import com.olibra.news.data.local.dao.ArticleReadDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext appContext: Context): NewsDatabase {
        return Room.databaseBuilder(
            appContext,
            NewsDatabase::class.java,
            "news.db"
        ).build()
    }

    @Provides
    fun provideArticleDao(database: NewsDatabase): ArticleDao {
        return database.articleDao()
    }

    @Provides
    fun provideArticleReadDao(database: NewsDatabase): ArticleReadDao {
        return database.articleReadDao()
    }
}