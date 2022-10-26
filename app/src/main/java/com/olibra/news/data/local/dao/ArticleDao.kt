package com.olibra.news.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.olibra.news.data.local.model.ArticleEntity
import io.reactivex.Single

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articleEntities: List<ArticleEntity>)

    @Query("SELECT * FROM articles where category = :category")
    fun getByCategory(category: String): Single<List<ArticleEntity>>
}