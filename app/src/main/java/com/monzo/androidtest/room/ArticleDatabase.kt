package com.monzo.androidtest.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.monzo.androidtest.articles.model.Article

@Database(entities = [Article::class], version = 1)
@TypeConverters(com.monzo.androidtest.room.TypeConverters::class)
abstract class ArticlesDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao

    companion object {
        private var instance: ArticlesDatabase? = null

        fun getInstance(context: Context): ArticlesDatabase {
            if (instance == null) {
                synchronized(ArticlesDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ArticlesDatabase::class.java,
                        "articles.db"
                    ).build()
                }
            }
            return instance!!
        }
    }
}