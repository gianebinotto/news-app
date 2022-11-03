package com.olibra.news.presentation.articledetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.olibra.news.R
import com.olibra.news.domain.model.Article
import com.olibra.news.domain.model.ErrorException
import com.olibra.news.domain.usecase.GetArticleUseCase
import com.olibra.news.domain.usecase.IsArticleReadUseCase
import com.olibra.news.domain.usecase.MarkArticleAsReadUseCase
import com.olibra.news.domain.usecase.MarkArticleAsUnreadUseCase
import com.olibra.news.presentation.common.extensions.addAction
import com.olibra.news.presentation.common.extensions.format
import com.olibra.news.presentation.common.extensions.removeAction
import com.olibra.news.presentation.common.extensions.setState
import com.olibra.news.presentation.common.viewmodel.DisposableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ArticleDetailsViewModel @Inject constructor(
    private val getArticleUseCase: GetArticleUseCase,
    private val isArticleReadUseCase: IsArticleReadUseCase,
    private val markArticleAsReadUseCase: MarkArticleAsReadUseCase,
    private val markArticleAsUnreadUseCase: MarkArticleAsUnreadUseCase
): DisposableViewModel() {

    private var article: Article? = null

    private val _state = MutableLiveData(ArticleDetailsState())
    val state: LiveData<ArticleDetailsState> = _state

    private val _actions = MutableLiveData<List<ArticleDetailsAction>>(emptyList())
    val actions: LiveData<List<ArticleDetailsAction>> = _actions

    fun onCreate(articleId: String?) {
        articleId?.let {
            getArticleById(articleId)
            getArticleReadStatus(articleId)
        }
    }

    fun onActionConsumed(actionId: String) {
        _actions.removeAction(actionId)
    }

    fun onRetryClicked(articleId: String?) {
        articleId?.let {
            getArticleById(it)
            getArticleReadStatus(it)
        }
    }

    fun onReadMoreClicked() {
        article?.readMoreUrl?.let { readMoreUrl ->
            _actions.addAction(ArticleDetailsAction.OpenReadMoreUrl(readMoreUrl))
        }
    }

    fun onReadStatusChanged() {
        if(_state.value?.isArticleRead ==  true) {
            markArticleAsUnread()
        } else {
            markArticleAsRead()
        }
    }

    private fun markArticleAsRead() {
        article?.let {
            markArticleAsReadUseCase(it.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onMarkArticleAsReadSucceed) {
                    onMarkArticleAsReadFailed()
                }
                .addDisposable()
        }
    }

    private fun onMarkArticleAsReadSucceed() {
        _state.setState { state-> state?.copy(isArticleRead = true) }
    }

    private fun onMarkArticleAsReadFailed() {
        _actions.addAction(ArticleDetailsAction.ShowError(
            errorMsg = R.string.mark_article_as_read_error,
            errorActionMsg = null
        ))
    }

    private fun markArticleAsUnread() {
        article?.let {
            markArticleAsUnreadUseCase(it.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onMarkArticleAsUnreadSucceed) {
                    onMarkArticleAsUnreadFailed()
                }
                .addDisposable()
        }
    }

    private fun onMarkArticleAsUnreadSucceed() {
        _state.setState { state-> state?.copy(isArticleRead = false) }
    }

    private fun onMarkArticleAsUnreadFailed() {
        _actions.addAction(ArticleDetailsAction.ShowError(
            errorMsg = R.string.mark_article_as_unread_error,
            errorActionMsg = null
        ))
    }

    private fun getArticleById(articleId: String) {
        getArticleUseCase(articleId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onGetArticleSucceed,::onGetArticleFailed)
            .addDisposable()
    }

    private fun onGetArticleSucceed(article: Article) {
        this.article = article

        with(article) {
            _state.setState { state ->
                state?.copy(
                    isEmptyState = false,
                    author = author,
                    title = title,
                    content = content,
                    image = imageUrl,
                    date = date.format(),
                    hasReadMore = readMoreUrl.isNullOrEmpty().not()
                )
            }
        }
    }

    private fun onGetArticleFailed(throwable: Throwable) {
        _state.setState { state -> state?.copy(isEmptyState = true) }
        _actions.addAction(getShowErrorAction(throwable))
    }

    private fun getArticleReadStatus(articleId: String) {
        isArticleReadUseCase(articleId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onGetArticleReadStatusSucceed, ::onGetArticleReadStatusFailed)
            .addDisposable()
    }

    private fun onGetArticleReadStatusSucceed(isArticleRead: Boolean) {
        _state.setState { state -> state?.copy(isArticleRead = isArticleRead) }
    }

    private fun onGetArticleReadStatusFailed(throwable: Throwable) {
        _actions.addAction(getShowErrorAction(throwable))
    }

    private fun getShowErrorAction(throwable: Throwable): ArticleDetailsAction.ShowError {
        val errorMsg = if(throwable is ErrorException.DatabaseErrorException.SqliteException) {
            R.string.database_error
        } else {
            R.string.generic_error
        }

        return ArticleDetailsAction.ShowError(errorMsg, R.string.retry)
    }
}