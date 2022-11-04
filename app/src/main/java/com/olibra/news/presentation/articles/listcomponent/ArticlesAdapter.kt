package com.olibra.news.presentation.articles.listcomponent

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import com.olibra.news.R
import com.olibra.news.presentation.articles.viewobject.ArticleViewObject

class ArticlesAdapter(
    private val onArticleClicked: (String, ImageView) -> Unit
) : ListAdapter<ArticleViewObject, ArticleViewHolder>(ArticleDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.view_list_item_article,
            parent,
            false
        )

        return ArticleViewHolder(view, onArticleClicked)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}