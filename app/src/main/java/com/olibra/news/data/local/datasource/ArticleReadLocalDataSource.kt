package com.olibra.news.data.local.datasource

import io.reactivex.Completable
import io.reactivex.Single

interface ArticleReadLocalDataSource {
    fun getArticlesRead(): Single<List<String>>
    fun addArticleRead(articleId: String): Completable
    fun removeArticleRead(articleId: String): Completable
    fun isArticleRead(articleId: String): Single<Boolean>
}