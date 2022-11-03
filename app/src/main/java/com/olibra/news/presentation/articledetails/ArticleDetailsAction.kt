package com.olibra.news.presentation.articledetails

import androidx.annotation.StringRes
import com.olibra.news.presentation.common.action.UIAction
import java.util.UUID

sealed class ArticleDetailsAction: UIAction {
    override val uniqueId: String = UUID.randomUUID().toString()

    data class ShowError(
        @StringRes val errorMsg: Int,
        @StringRes val errorActionMsg: Int?
    ): ArticleDetailsAction()
    data class OpenReadMoreUrl(val url: String): ArticleDetailsAction()
}