package com.olibra.news.commontest

import com.olibra.news.domain.model.Article
import java.text.SimpleDateFormat
import java.util.Date

object ArticlesDataTest {

    fun getArticle(id: String): Article {
        return Article(
            id = id,
            title = "Article title",
            content = "Content here",
            author = "John Doe",
            date = getFixedDate(),
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

    private fun getFixedDate(): Date {
        val date = "11/01/2022"
        val format = SimpleDateFormat("MM/dd/yyyy")
        return format.parse(date) ?: Date()
    }
}