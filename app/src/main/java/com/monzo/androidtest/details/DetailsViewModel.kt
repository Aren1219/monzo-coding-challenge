package com.monzo.androidtest.details

import androidx.lifecycle.LiveData
import com.monzo.androidtest.api.model.ApiArticle
import com.monzo.androidtest.articles.ArticlesRepository
import com.monzo.androidtest.articles.model.Article
import com.monzo.androidtest.common.BaseViewModel
import com.monzo.androidtest.common.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DetailsViewModel(
    private val repository: ArticlesRepository,
) : BaseViewModel<DetailsState>(DetailsState()) {

    private val savedArticlesLiveData: LiveData<List<Article>> = repository.getDatabaseList()

    init {
        savedArticlesLiveData.observeForever {
            setState {
                copy(savedArticles = it)
            }
        }
    }

    fun getArticle(url: String) {
        if (url.isNotEmpty()) {
            setState { copy(refreshing = true) }
            disposables += repository.getArticle(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { article ->
                    setState { copy(refreshing = false, article = article) }
                }
        }
    }

    suspend fun deleteOrInsertArticle(): Boolean? {
        return if (state.value != null && state.value!!.article != null) {
            repository.deleteOrInsertArticle(article = state.value!!.article!!)
        } else
            null
    }
}

data class DetailsState(
    val refreshing: Boolean = true,
    val article: ApiArticle? = null,
    val savedArticles: List<Article> = emptyList()
)