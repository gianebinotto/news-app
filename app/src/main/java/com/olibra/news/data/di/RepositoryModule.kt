package com.olibra.news.data.di

import com.olibra.news.data.repository.ArticleReadRepositoryImpl
import com.olibra.news.data.repository.ArticleRepositoryImpl
import com.olibra.news.domain.repository.ArticleReadRepository
import com.olibra.news.domain.repository.ArticleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindArticleRepository(
        articleRepositoryImpl: ArticleRepositoryImpl
    ): ArticleRepository

    @Binds
    abstract fun bindArticleReadRepository(
        articleReadRepositoryImpl: ArticleReadRepositoryImpl
    ): ArticleReadRepository
}