package com.olibra.news.presentation.articles

import com.olibra.news.presentation.articles.viewobject.ArticleViewObject
import com.olibra.news.presentation.common.state.UIState

data class ArticlesState(
    var isLoading: Boolean = false,
    var isEmptyState: Boolean = false,
    var articles: List<ArticleViewObject>? = null
): UIState