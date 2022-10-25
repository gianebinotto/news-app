package com.olibra.news.domain.repository

import io.reactivex.Single

interface ArticleReadRepository {
    fun getAll(): Single<String>
    fun add(articleId: String)
    fun remove(articleId: String)
}