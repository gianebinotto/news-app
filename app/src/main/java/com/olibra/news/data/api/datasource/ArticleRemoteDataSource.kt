package com.olibra.news.data.api.datasource

import com.olibra.news.domain.model.Article
import io.reactivex.Single

interface ArticleRemoteDataSource {
    fun getArticles(category: String): Single<List<Article>>
}