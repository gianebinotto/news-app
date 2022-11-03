package com.olibra.news.domain.usecase

import com.olibra.news.domain.model.Article
import com.olibra.news.domain.repository.ArticleRepository
import io.reactivex.Single
import javax.inject.Inject

class GetArticleUseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {
    operator fun invoke(articleId: String): Single<Article> {
        return articleRepository.getArticleById(articleId)
    }
}