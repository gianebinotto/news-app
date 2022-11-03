package com.olibra.news.data.api.service

import com.olibra.news.data.api.model.ArticlesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleService {

    @GET("news")
    fun getArticles(
        @Query("category") category: String
    ): Single<ArticlesResponse>
}