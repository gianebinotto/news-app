package com.olibra.news.data.local.datasource

import com.olibra.news.domain.model.Article
import io.reactivex.Single

interface ArticleLocalDataSource {
    fun getArticles(category: String): Single<List<Article>>
    fun addArticles(articles: List<Article>, category: String)
}