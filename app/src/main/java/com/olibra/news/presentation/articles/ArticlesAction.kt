package com.olibra.news.presentation.articles

import androidx.annotation.StringRes
import com.olibra.news.presentation.common.action.UIAction
import java.util.UUID

sealed class ArticlesAction: UIAction {
    override val uniqueId: String = UUID.randomUUID().toString()

    data class ShowError(
        @StringRes val errorMsg: Int,
        @StringRes val errorActionMsg: Int?
    ): ArticlesAction()
    data class NavigateToArticle(val articleId: String): ArticlesAction()
    object StopSwipeRefresh: ArticlesAction()
}