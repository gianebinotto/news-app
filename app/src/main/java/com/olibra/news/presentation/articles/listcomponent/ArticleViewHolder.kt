package com.olibra.news.presentation.articles.listcomponent

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.olibra.news.databinding.ViewListItemArticleBinding
import com.olibra.news.presentation.articles.viewobject.ArticleViewObject
import com.olibra.news.presentation.common.extensions.format
import com.olibra.news.presentation.common.extensions.load

class ArticleViewHolder(
    view: View,
    private val onArticleClicked: (String) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val binding: ViewListItemArticleBinding = ViewListItemArticleBinding.bind(view)

    fun bind(articleViewObject: ArticleViewObject) {
        with(binding) {
            articleImageView.load(itemView.context, articleViewObject.imageUrl)
            articleDateTextView.text = articleViewObject.date.format()
            articleTitleTextView.text = articleViewObject.title
            articleAuthorTextView.text = articleViewObject.author
            articleReadImageView.isVisible = articleViewObject.isRead

            root.setOnClickListener {
                onArticleClicked(articleViewObject.id)
            }
        }
    }
}