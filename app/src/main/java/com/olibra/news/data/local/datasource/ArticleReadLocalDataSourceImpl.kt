package com.olibra.news.data.local.datasource

import com.olibra.news.data.local.dao.ArticleReadDao
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
        }
    }

    override fun addArticleRead(articleId: String): Completable {
        val articleEntity = ArticleReadEntity(articleId = articleId)
        return articleReadDao.insert(articleEntity)
    }

    override fun removeArticleRead(articleId: String): Completable {
        return articleReadDao.deleteByArticleId(articleId)
    }
}