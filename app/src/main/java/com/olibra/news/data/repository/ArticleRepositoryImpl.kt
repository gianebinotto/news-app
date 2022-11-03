package com.olibra.news.data.repository

import android.annotation.SuppressLint
import com.olibra.news.data.api.datasource.ArticleRemoteDataSource
import com.olibra.news.data.local.datasource.ArticleLocalDataSource
import com.olibra.news.domain.model.Article
import com.olibra.news.domain.repository.ArticleRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor (
    private val remoteDataSource: ArticleRemoteDataSource,
    private val localDataSource: ArticleLocalDataSource
): ArticleRepository {

    @SuppressLint("CheckResult")
    override fun getArticles(category: String): Flowable<List<Article>> {
        return Flowable.create({ emitter ->
            localDataSource
                .getArticles(category)
                .subscribe(
                    emitter::onNext,
                    emitter::tryOnError
                )
        }, BackpressureStrategy.LATEST)
    }

    override fun getArticleById(articleId: String): Single<Article> {
        return localDataSource.getArticleById(articleId)
    }

    override fun updateArticles(category: String): Completable {
        return remoteDataSource.getArticles(category)
            .flatMapCompletable { articles ->
                localDataSource.addArticles(articles, category)
            }
    }
}