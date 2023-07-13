package com.monzo.androidtest.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.monzo.androidtest.articles.model.Article


@Dao
interface ArticleDao {
    @Delete
    suspend fun deleteArticle(article: Article): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticle(article: Article)

    @Query("SELECT * FROM articles")
    fun getArticles(): LiveData<List<Article>>
}
