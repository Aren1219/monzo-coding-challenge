package com.monzo.androidtest.articles

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.monzo.androidtest.HeadlinesApp
import com.monzo.androidtest.R
import com.monzo.androidtest.details.DetailsActivity

class ArticlesActivity : AppCompatActivity(), ArticleAdapter.OnItemClickListener {
    private lateinit var viewModel: ArticlesViewModel
    private lateinit var adapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.articles_swiperefreshlayout)
        val recyclerView = findViewById<RecyclerView>(R.id.articles_recyclerview)

        setSupportActionBar(toolbar)

        viewModel = HeadlinesApp.from(applicationContext).inject(this)

        adapter = ArticleAdapter(this, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener { viewModel.onRefresh() }

        viewModel.state.observe(this) { state ->
            swipeRefreshLayout.isRefreshing = state.refreshing
            if (state.savedArticles.isNotEmpty()) {
                adapter.showArticles(state.savedArticles + state.articles, state.savedArticles.size)
            } else {
                adapter.showArticles(state.articles, 0)
            }
        }
    }

    override fun onItemClick(articleUrl: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("Url", articleUrl)
        startActivity(intent)
    }
}