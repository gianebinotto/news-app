package com.olibra.news.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "articlesRead",
    foreignKeys = [ForeignKey(
        entity = ArticleEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("article"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ArticleReadEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(index = true)
    val article: String
)