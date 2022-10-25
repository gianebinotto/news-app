package com.olibra.news.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleResponse(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("author") val author: String,
    @SerialName("date") val date: String,
    @SerialName("content") val content: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("readMoreUrl") val readMoreUrl: String? = null
)