package com.olibra.news.presentation.articles.listcomponent

import androidx.recyclerview.widget.DiffUtil
import com.olibra.news.presentation.articles.viewobject.ArticleViewObject

class ArticleDiffUtil : DiffUtil.ItemCallback<ArticleViewObject>() {
    override fun areItemsTheSame(oldItem: ArticleViewObject, newItem: ArticleViewObject): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: ArticleViewObject, newItem: ArticleViewObject): Boolean {
        return oldItem == newItem
    }
}