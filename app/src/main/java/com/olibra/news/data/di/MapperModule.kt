package com.olibra.news.data.di

import com.olibra.news.data.api.mapper.ArticleRemoteMapper
import com.olibra.news.data.local.mapper.ArticleLocalMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object MapperModule {

    @Provides
    fun provideArticleRemoteMapper(): ArticleRemoteMapper {
        return ArticleRemoteMapper()
    }

    @Provides
    fun provideArticleLocalMapper(): ArticleLocalMapper {
        return ArticleLocalMapper()
    }
}