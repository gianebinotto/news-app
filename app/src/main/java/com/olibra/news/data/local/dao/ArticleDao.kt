package com.olibra.news.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.olibra.news.data.local.model.ArticleEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articleEntities: List<ArticleEntity>): Completable

    @Query("SELECT * FROM articles where category = :category")
    fun getByCategory(category: String): Flowable<List<ArticleEntity>>

    @Query("SELECT * FROM articles where id = :id")
    fun getById(id: String): Single<ArticleEntity>
}