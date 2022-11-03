package com.olibra.news.data.api.datasource

import com.olibra.news.data.api.mapper.ArticleRemoteMapper
import com.olibra.news.data.api.service.ArticleService
import com.olibra.news.domain.model.Article
import com.olibra.news.domain.model.ErrorException
import io.reactivex.Single
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.UnknownHostException
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
        }.handleApiException()
    }

    private fun Single<List<Article>>.handleApiException() = onErrorResumeNext {
        val errorException = when (it) {
            is UnknownHostException -> ErrorException.ApiErrorException.NetworkConnectionException
            is HttpException -> {
                when (it.code()) {
                    HttpURLConnection.HTTP_NOT_FOUND ->
                        ErrorException.ApiErrorException.NotFoundException
                    HttpURLConnection.HTTP_UNAVAILABLE ->
                        ErrorException.ApiErrorException.ServiceUnavailableException
                    else -> ErrorException.ApiErrorException.UnknownException
                }
            }
            else -> ErrorException.ApiErrorException.UnknownException
        }
        Single.error(errorException)
    }
}