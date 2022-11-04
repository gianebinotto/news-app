package com.olibra.news.presentation.articles.listcomponent

import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.olibra.news.databinding.ViewListItemArticleBinding
import com.olibra.news.presentation.articles.viewobject.ArticleViewObject
import com.olibra.news.presentation.common.extensions.format
import com.olibra.news.presentation.common.extensions.load

private const val TRANSITION_COMPONENT = "transitionComponent"

class ArticleViewHolder(
    view: View,
    private val onArticleClicked: (String, ImageView) -> Unit
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
                onArticleClicked(articleViewObject.id, articleImageView)
            }

            articleImageView.transitionName = "$TRANSITION_COMPONENT$adapterPosition"
        }
    }
}