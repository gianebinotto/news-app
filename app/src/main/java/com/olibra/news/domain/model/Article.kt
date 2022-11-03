package com.olibra.news.domain.model

import java.util.Date

data class Article(
    val id: String,
    val title: String,
    val content: String,
    val author: String,
    val date: Date,
    val imageUrl: String,
    val readMoreUrl: String?,
    val category: String
)