package com.olibra.news.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val date: String,
    val content: String,
    val category: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "read_more_url") val readMoreUrl: String?
)