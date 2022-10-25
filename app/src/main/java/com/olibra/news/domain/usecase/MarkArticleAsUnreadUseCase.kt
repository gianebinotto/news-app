package com.olibra.news.domain.usecase

import com.olibra.news.domain.repository.ArticleReadRepository
import javax.inject.Inject

class MarkArticleAsUnreadUseCase @Inject constructor(
    private val articleReadRepository: ArticleReadRepository
) {
    operator fun invoke(articleId: String) {
        return articleReadRepository.remove(articleId)
    }
}