package com.olibra.news.data.repository

import com.olibra.news.data.local.datasource.ArticleReadLocalDataSource
import com.olibra.news.domain.repository.ArticleReadRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ArticleReadRepositoryImpl @Inject constructor(
    private val articleReadLocalDataSource: ArticleReadLocalDataSource
): ArticleReadRepository {

    override fun getArticlesRead(): Single<List<String>> {
        return articleReadLocalDataSource.getArticlesRead()
    }

    override fun isArticleRead(articleId: String): Single<Boolean> {
        return articleReadLocalDataSource.isArticleRead(articleId)
    }

    override fun addArticleRead(articleId: String): Completable {
        return articleReadLocalDataSource.addArticleRead(articleId)
    }

    override fun removeArticleRead(articleId: String): Completable {
        return articleReadLocalDataSource.removeArticleRead(articleId)
    }
}