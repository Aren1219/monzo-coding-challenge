package com.monzo.androidtest.articles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monzo.androidtest.R
import com.monzo.androidtest.articles.model.Article
import java.util.*

internal class ArticleAdapter(
    ctx: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var context: Context? = null
    private var articles: MutableList<Article> = ArrayList()
    private var savedArticlesCount = 0

    init {
        context = ctx
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.list_item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val articleViewHolder = holder as ArticleViewHolder
        if (savedArticlesCount >= 1) {
            when (position) {
                0 -> articleViewHolder.bind(articles[position], "Saved articles")
                savedArticlesCount -> articleViewHolder.bind(articles[position], "Recent articles")
                else -> articleViewHolder.bind(articles[position])
            }
        } else {
            if (position == 0)
                articleViewHolder.bind(articles[position], "Recent articles")
            else
                articleViewHolder.bind(articles[position])
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun showArticles(articles: List<Article>, savedArticleCount: Int) {
        if (articles.isNotEmpty()) {
            this.articles = articles as MutableList<Article>
            this.savedArticlesCount = savedArticleCount
            notifyDataSetChanged()
        }
    }

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val articleId = articles[position].url
                    listener.onItemClick(articleId)
                }
            }
        }

        fun bind(article: Article, sectionTitle: String = "") {
            val headlineView = itemView.findViewById<TextView>(R.id.article_headline_textview)
            val thumbnailView = itemView.findViewById<ImageView>(R.id.article_thumbnail_imageview)
            val sectionTitleView = itemView.findViewById<TextView>(R.id.section_title_textview)

            if (sectionTitle.isNotEmpty()) {
                sectionTitleView.height = 50
            } else {
                sectionTitleView.height = 0
            }
            sectionTitleView.text = sectionTitle
            headlineView.text = article.title
            Glide.with(context!!).load(article.thumbnail).into(thumbnailView)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(articleUrl: String)
    }
}
