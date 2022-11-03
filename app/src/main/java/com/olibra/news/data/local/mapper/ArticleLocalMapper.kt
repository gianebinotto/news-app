package com.olibra.news.data.local.mapper

import com.olibra.news.data.local.model.ArticleEntity
import com.olibra.news.domain.model.Article

class ArticleLocalMapper {

    fun fromEntityToDomain(articleEntity: ArticleEntity, category: String): Article {
        return with(articleEntity) {
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

    fun fromDomainToEntity(article: Article, category: String): ArticleEntity {
        return with(article) {
            ArticleEntity(
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