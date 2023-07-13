package com.monzo.androidtest.articles.model

import com.monzo.androidtest.api.model.ApiArticle
import com.monzo.androidtest.api.model.ApiArticleListResponse
import java.util.*

class ArticleMapper {

    fun map(apiArticleListResponse: ApiArticleListResponse): List<Article> {
        val articles = ArrayList<Article>()

        for ((id, sectionId, sectionName, webPublicationDate, _, _, apiUrl, fields) in apiArticleListResponse.response.results) {

            var thumbnail: String
            if (fields == null) {
                thumbnail = ""
            } else {
                thumbnail = fields.thumbnail ?: ""
            }

            var headline: String
            if (fields == null) {
                headline = ""
            } else {
                headline = fields.headline ?: ""
            }

            articles.add(
                Article(
                    id,
                    thumbnail,
                    sectionId,
                    sectionName,
                    webPublicationDate,
                    headline,
                    apiUrl
                )
            )
        }

        return articles
    }

    fun map(apiArticle: ApiArticle): Article {
        return Article(
            id = apiArticle.id,
            thumbnail = apiArticle.fields?.thumbnail ?: "",
            sectionId = apiArticle.sectionId,
            sectionName = apiArticle.sectionName,
            published = apiArticle.webPublicationDate,
            title = apiArticle.webTitle,
            url = apiArticle.apiUrl
        )
    }
}
