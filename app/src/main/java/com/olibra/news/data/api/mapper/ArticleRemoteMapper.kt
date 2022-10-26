package com.olibra.news.data.api.mapper

import com.olibra.news.data.api.model.ArticleResponse
import com.olibra.news.domain.model.Article

class ArticleRemoteMapper {

    fun fromResponseToDomain(articleResponse: ArticleResponse, category: String): Article {
        return with(articleResponse) {
            Article(
                id = id,
                title = title,
                content = content,
                author = author,
                date = date,
                imageUrl = imageUrl,
                readMoreUrl = readMoreUrl,
                category = category
            )
        }
    }
}