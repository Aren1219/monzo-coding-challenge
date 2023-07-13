package com.monzo.androidtest.api.model

data class ApiDetailsResponse(
    val response: ApiDetails
)

data class ApiDetails(
    val content: ApiArticle
)