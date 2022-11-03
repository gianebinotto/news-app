package com.olibra.news.domain.usecase

import com.olibra.news.domain.repository.ArticleReadRepository
import io.reactivex.Single
import javax.inject.Inject

class GetArticlesReadUseCase @Inject constructor(
    private val articleReadRepository: ArticleReadRepository
) {
    operator fun invoke(): Single<List<String>> {
        return articleReadRepository.getArticlesRead()
    }
}