package com.monzo.androidtest.articles

import com.monzo.androidtest.api.GuardianService
import com.monzo.androidtest.api.model.ApiArticle
import com.monzo.androidtest.articles.model.Article
import com.monzo.androidtest.articles.model.ArticleMapper
import com.monzo.androidtest.room.ArticleDao
import io.reactivex.Single

class ArticlesRepository(
    private val guardianService: GuardianService,
    private val articleMapper: ArticleMapper,
    private val articleDao: ArticleDao
) {
    fun latestFintechArticles(): Single<List<Article>> {
        return guardianService.searchArticles("fintech,brexit")
            .map { articleMapper.map(it) }
    }

    fun getArticle(articleUrl: String): Single<ApiArticle> {
        return guardianService.getArticle(articleUrl, "main,body,headline,thumbnail").map {
            it.response.content
        }
    }

    //returns true if deleted, false if inserted
    suspend fun deleteOrInsertArticle(article: ApiArticle): Boolean {
        return if (articleDao.deleteArticle(articleMapper.map(article)) == 0) {
            articleDao.insertArticle(articleMapper.map(article))
            false
        } else
            true
    }

    fun getDatabaseList() = articleDao.getArticles()
}
