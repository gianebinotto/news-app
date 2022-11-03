package com.olibra.news.domain.usecase

import com.olibra.news.domain.model.Article
import com.olibra.news.domain.repository.ArticleRepository
import io.reactivex.Flowable
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor (
    private val repository: ArticleRepository
) {
    operator fun invoke(category: String): Flowable<List<Article>> {
        return repository.getArticles(category)
    }
}