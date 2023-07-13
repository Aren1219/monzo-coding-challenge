package com.monzo.androidtest.details

import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.monzo.androidtest.HeadlinesApp
import com.monzo.androidtest.R
import com.monzo.androidtest.articles.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsActivity : AppCompatActivity() {
    private lateinit var viewModel: DetailsViewModel
    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_details)

        viewModel = HeadlinesApp.from(applicationContext).injectDetailsViewModel(this)

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.details_swiperefreshlayout)
        val title = findViewById<TextView>(R.id.details_title_textview)
        val image = findViewById<ImageView>(R.id.details_thumbnail_imageview)
        val backButton = findViewById<ImageView>(R.id.details_back_button_imageview)
        val webView = findViewById<WebView>(R.id.details_content_textview)
        val saveButton = findViewById<ImageView>(R.id.details_save_button_imageview)

        url = intent.extras?.getString("Url") ?: ""

        backButton.setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    if (viewModel.deleteOrInsertArticle() == true) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                applicationContext,
                                "Article deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                applicationContext,
                                "Article saved",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }


        swipeRefreshLayout.setOnRefreshListener { viewModel.getArticle(url) }

        if (url.isNotEmpty()) {
            viewModel.getArticle(url)
        }

        //observe ViewModel state
        viewModel.state.observe(this) { state ->

            swipeRefreshLayout.isRefreshing = state.refreshing

            if (state.article != null) {
                val article = state.article
                title.text = article.webTitle
                Glide.with(this).load(article.fields?.thumbnail).into(image)
                webView.loadData(article.fields!!.body!!, "text/html", "UTF-8")

                //change button icon
                if (state.savedArticles.firstOrNull { it.id == state.article.id } != null) {
                    saveButton.setImageDrawable(
                        AppCompatResources.getDrawable(this, android.R.drawable.ic_delete)
                    )
                } else {
                    saveButton.setImageDrawable(
                        AppCompatResources.getDrawable(this, android.R.drawable.ic_menu_save)
                    )
                }
            }
        }
    }
}