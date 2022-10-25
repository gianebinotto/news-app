package com.olibra.news.domain.repository

import com.olibra.news.domain.model.Article
import io.reactivex.Flowable

interface ArticleRepository {
    fun getArticles(category: String): Flowable<List<Article>>
}