package com.olibra.news.data.local.datasource

import com.olibra.news.domain.model.Article
import io.reactivex.Completable
import io.reactivex.Flowable

interface ArticleLocalDataSource {
    fun getArticles(category: String): Flowable<List<Article>>
    fun addArticles(articles: List<Article>, category: String): Completable
}