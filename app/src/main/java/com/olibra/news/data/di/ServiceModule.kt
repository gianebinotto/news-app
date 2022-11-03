package com.olibra.news.data.di

import com.olibra.news.data.api.service.ArticleService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    fun provideArticleService(
        networkClient: Retrofit
    ): ArticleService {
        return networkClient.create(ArticleService::class.java)
    }
}