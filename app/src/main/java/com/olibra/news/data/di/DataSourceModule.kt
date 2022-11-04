package com.olibra.news.data.di

import com.olibra.news.data.api.datasource.ArticleRemoteDataSource
import com.olibra.news.data.api.datasource.ArticleRemoteDataSourceImpl
import com.olibra.news.data.local.datasource.ArticleLocalDataSource
import com.olibra.news.data.local.datasource.ArticleLocalDataSourceImpl
import com.olibra.news.data.local.datasource.ArticleReadLocalDataSource
import com.olibra.news.data.local.datasource.ArticleReadLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindArticleRemoteDataSource(
        articleRemoteDataSource: ArticleRemoteDataSourceImpl
    ): ArticleRemoteDataSource

    @Binds
    abstract fun bindArticleLocalDataSource(
        articleLocalDataSource: ArticleLocalDataSourceImpl
    ): ArticleLocalDataSource

    @Binds
    abstract fun bindArticleReadLocalDataSource(
        articleReadLocalDataSource: ArticleReadLocalDataSourceImpl
    ): ArticleReadLocalDataSource
}