package com.olibra.news.presentation.articles.viewobject

import java.util.Date

data class ArticleViewObject (
    val id: String,
    val title: String,
    val author: String,
    val date: Date,
    val imageUrl: String,
    val isRead: Boolean
)