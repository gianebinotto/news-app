package com.olibra.news.domain.repository

import io.reactivex.Completable
import io.reactivex.Single

interface ArticleReadRepository {
    fun getArticlesRead(): Single<List<String>>
    fun isArticleRead(articleId: String): Single<Boolean>
    fun addArticleRead(articleId: String): Completable
    fun removeArticleRead(articleId: String): Completable
}