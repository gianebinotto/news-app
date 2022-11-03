package com.olibra.news.domain.repository

import com.olibra.news.domain.model.Article
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface ArticleRepository {
    fun getArticles(category: String): Flowable<List<Article>>
    fun getArticleById(articleId: String): Single<Article>
    fun updateArticles(category: String): Completable
}