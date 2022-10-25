package com.olibra.news.domain.model

data class Article(
    val id: String,
    val title: String,
    val content: String,
    val author: String,
    val date: String,
    val imageUrl: String,
    val readMoreUrl: String?,
    val category: String
)