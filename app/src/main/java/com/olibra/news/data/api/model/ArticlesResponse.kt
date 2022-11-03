package com.olibra.news.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticlesResponse(
    @SerialName("category") val category: String,
    @SerialName("data") val articles: List<ArticleResponse>
)