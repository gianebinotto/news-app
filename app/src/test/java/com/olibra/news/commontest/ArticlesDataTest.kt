package com.olibra.news.commontest

import com.olibra.news.domain.model.Article

object ArticlesDataTest {

    fun getArticle(id: String): Article {
        return Article(
            id = id,
            title = "Article title",
            content = "Content here",
            author = "John Doe",
            date = getFixedDate("11/03/2022"),
            imageUrl = "Image url here",
            readMoreUrl = "Read more url here",
            category = "all"
        )
    }

    fun getArticles(): List<Article> {
        val articles = mutableListOf<Article>()
        for (id in 1.. 10) {
            articles.add(getArticle(id.toString()))
        }
        return articles
    }
}