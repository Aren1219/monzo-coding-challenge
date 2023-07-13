package com.monzo.androidtest.articles

import com.monzo.androidtest.articles.model.Article
import com.monzo.androidtest.common.BaseViewModel
import com.monzo.androidtest.common.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ArticlesViewModel(
    private val repository: ArticlesRepository
) : BaseViewModel<ArticlesState>(ArticlesState()) {

    private val savedArticlesLiveData = repository.getDatabaseList()

    init {
        onRefresh()
        savedArticlesLiveData.observeForever {
            setState {
                copy(savedArticles = it)
            }
        }
    }

    fun onRefresh() {
        setState { copy(refreshing = true) }
        disposables += repository.latestFintechArticles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { articles ->
                setState { copy(refreshing = false, articles = articles) }
            }
    }
}

data class ArticlesState(
    val refreshing: Boolean = true,
    val articles: List<Article> = emptyList(),
    val savedArticles: List<Article> = emptyList()
)
