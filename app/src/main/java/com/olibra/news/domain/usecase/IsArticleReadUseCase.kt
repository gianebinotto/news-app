package com.olibra.news.domain.usecase

import com.olibra.news.domain.repository.ArticleReadRepository
import io.reactivex.Single
import javax.inject.Inject

class IsArticleReadUseCase @Inject constructor(
    private val articleReadRepository: ArticleReadRepository
) {
    operator fun invoke(articleId: String): Single<Boolean> {
        return articleReadRepository.isArticleRead(articleId)
    }
}