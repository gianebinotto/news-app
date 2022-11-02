package com.olibra.news.data.api.model

import com.olibra.news.data.api.serializer.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class ArticleResponse(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("author") val author: String,
    @Serializable(DateSerializer::class) @SerialName("date") val date: Date,
    @SerialName("content") val content: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("readMoreUrl") val readMoreUrl: String? = null
)