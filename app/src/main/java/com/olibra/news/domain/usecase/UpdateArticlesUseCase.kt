package com.olibra.news.domain.usecase

import com.olibra.news.domain.repository.ArticleRepository
import io.reactivex.Completable
import javax.inject.Inject

class UpdateArticlesUseCase @Inject constructor (
    private val articleRepository: ArticleRepository
) {
    operator fun invoke(category: String): Completable {
        return articleRepository.updateArticles(category)
    }
}