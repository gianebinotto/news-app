package com.olibra.news.domain.usecase

import com.olibra.news.domain.repository.ArticleReadRepository
import io.reactivex.Completable
import javax.inject.Inject

class MarkArticleAsReadUseCase @Inject constructor(
    private val articleReadRepository: ArticleReadRepository
) {
    operator fun invoke(articleId: String): Completable {
        return articleReadRepository.addArticleRead(articleId)
    }
}