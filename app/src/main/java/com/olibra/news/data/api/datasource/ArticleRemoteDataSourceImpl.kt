package com.olibra.news.data.api.datasource

import com.olibra.news.data.api.mapper.ArticleRemoteMapper
import com.olibra.news.data.api.service.ArticleService
import com.olibra.news.domain.model.Article
import io.reactivex.Single
import javax.inject.Inject

class ArticleRemoteDataSourceImpl @Inject constructor(
    private val articleService: ArticleService,
    private val articleMapper: ArticleRemoteMapper
): ArticleRemoteDataSource {

    override fun getArticles(category: String): Single<List<Article>> {
        return articleService.getArticles(category).map { articleResponses ->
            articleResponses.articles.map { articleResponse ->
                articleMapper.fromResponseToDomain(articleResponse, category)
            }
        }
    }
}