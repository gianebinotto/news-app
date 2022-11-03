package com.olibra.news.presentation.articledetails

import com.olibra.news.presentation.common.state.UIState

data class ArticleDetailsState(
    var isLoading: Boolean = false,
    var isEmptyState: Boolean = false,
    var author: String? = String(),
    var title: String? = String(),
    var date: String? = String(),
    var image: String? = null,
    var content: String? = String(),
    var hasReadMore: Boolean = false,
    var isArticleRead: Boolean = false
): UIState