package com.olibra.news.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "articlesRead",
    indices = [Index(value = ["article_id"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = ArticleEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("article_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ArticleReadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "article_id")
    val articleId: String
)