package com.olibra.news.data.local.datasource

import com.olibra.news.data.local.dao.ArticleDao
import com.olibra.news.data.local.mapper.ArticleLocalMapper
import com.olibra.news.domain.model.Article
import io.reactivex.Single
import javax.inject.Inject

class ArticleLocalDataSourceImpl @Inject constructor(
    private val articleDao: ArticleDao,
    private val articleMapper: ArticleLocalMapper
): ArticleLocalDataSource {

    override fun getArticles(category: String): Single<List<Article>> {
        return articleDao.getByCategory(category).map { articleEntities ->
            articleEntities.map { articleEntity ->
                articleMapper.fromEntityToDomain(articleEntity, category)
            }
        }
    }

    override fun addArticles(articles: List<Article>, category: String) {
        val articleEntities = articles.map { articleMapper.fromDomainToEntity(it, category) }
        articleDao.insert(articleEntities)
    }
}