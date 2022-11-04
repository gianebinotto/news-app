package com.olibra.news.presentation.articles

import com.olibra.news.commontest.ArticlesDataTest
import com.olibra.news.domain.model.Article
import com.olibra.news.presentation.articles.viewobject.ArticleViewObject

object ArticlesViewObjectDataTest {

    fun getArticleViewObject(id: String, isRead: Boolean): ArticleViewObject {
        return ArticlesDataTest.getArticle(id).toViewObject(isRead)
    }

    fun getArticlesViewObjects(): List<ArticleViewObject> {
        return ArticlesDataTest.getArticles().map { it.toViewObject(false) }
    }

    fun Article.toViewObject(isRead: Boolean): ArticleViewObject {
        return ArticleViewObject(
            id = id,
            title = title,
            author = author,
            date = date,
            imageUrl = imageUrl,
            isRead = isRead
        )
    }
}