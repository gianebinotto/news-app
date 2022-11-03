package com.olibra.news.data.local.datasource

import com.olibra.news.data.local.dao.ArticleReadDao
import com.olibra.news.data.local.extension.handleDatabaseException
import com.olibra.news.data.local.model.ArticleReadEntity
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ArticleReadLocalDataSourceImpl @Inject constructor(
    private val articleReadDao: ArticleReadDao
): ArticleReadLocalDataSource {

    override fun getArticlesRead(): Single<List<String>> {
        return articleReadDao.getAll().map { articleReadEntities ->
            articleReadEntities.map { articleReadEntity ->
                articleReadEntity.articleId
            }
        }.handleDatabaseException()
    }

    override fun addArticleRead(articleId: String): Completable {
        val articleEntity = ArticleReadEntity(articleId = articleId)
        return articleReadDao.insert(articleEntity).handleDatabaseException()
    }

    override fun removeArticleRead(articleId: String): Completable {
        return articleReadDao.deleteByArticleId(articleId).handleDatabaseException()
    }

    override fun isArticleRead(articleId: String): Single<Boolean> {
        return articleReadDao.getByArticleId(articleId).isEmpty.map {
            it.not()
        }.handleDatabaseException()
    }
}