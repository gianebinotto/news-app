package com.olibra.news.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.olibra.news.data.local.model.ArticleReadEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ArticleReadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: ArticleReadEntity): Completable

    @Query("DELETE FROM articlesRead where article_id = :articleId")
    fun deleteByArticleId(articleId: String): Completable

    @Query("SELECT * FROM articlesRead")
    fun getAll(): Single<List<ArticleReadEntity>>
}