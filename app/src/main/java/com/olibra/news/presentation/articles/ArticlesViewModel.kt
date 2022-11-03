package com.olibra.news.presentation.articles

import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.olibra.news.R
import com.olibra.news.domain.model.Article
import com.olibra.news.domain.model.ErrorException.ApiErrorException.NetworkConnectionException
import com.olibra.news.domain.model.ErrorException.ApiErrorException.ServiceUnavailableException
import com.olibra.news.domain.model.ErrorException.ApiErrorException.NotFoundException
import com.olibra.news.domain.model.ErrorException.DatabaseErrorException.SqliteException
import com.olibra.news.domain.usecase.GetArticlesReadUseCase
import com.olibra.news.domain.usecase.GetArticlesUseCase
import com.olibra.news.domain.usecase.UpdateArticlesUseCase
import com.olibra.news.presentation.articles.viewobject.ArticleViewObject
import com.olibra.news.presentation.common.extensions.addAction
import com.olibra.news.presentation.common.extensions.removeAction
import com.olibra.news.presentation.common.extensions.setState
import com.olibra.news.presentation.common.viewmodel.DisposableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val updateArticlesUseCase: UpdateArticlesUseCase,
    private val getArticlesReadUseCase: GetArticlesReadUseCase
): DisposableViewModel() {

    private var selectedCategory: String = String()

    private val _state = MutableLiveData(ArticlesState())
    val state: LiveData<ArticlesState> = _state

    private val _actions = MutableLiveData<List<ArticlesAction>>(emptyList())
    val actions: LiveData<List<ArticlesAction>> = _actions

    fun onViewCreated(@IdRes menuItemId: Int?) {
        CategoryMenuIdRepresentation.getCategoryByMenuId(menuItemId)?.let {
            selectedCategory = it.category
            getArticles()
            refreshArticles(isFromSwipeToRefresh = false)
        }
    }

    fun onResume() {
        getArticles()
    }

    fun onArticleClicked(articleId: String) {
        _actions.addAction(ArticlesAction.NavigateToArticle(articleId))
    }

    fun onActionConsumed(actionId: String) {
        _actions.removeAction(actionId)
    }

    fun onSwipeToRefreshArticles() {
        refreshArticles(isFromSwipeToRefresh = true)
    }

    fun onRetryClicked() {
        refreshArticles(isFromSwipeToRefresh = false)
    }

    private fun refreshArticles(isFromSwipeToRefresh: Boolean) {
        updateArticlesUseCase(selectedCategory)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading(isFromSwipeToRefresh) }
            .doFinally { hideLoading(isFromSwipeToRefresh) }
            .subscribe({},::onRefreshArticlesError)
            .addDisposable()
    }

    private fun onRefreshArticlesError(throwable: Throwable) {
        _state.setState { it?.copy(isEmptyState = hasNoArticlesBeingShown()) }
        _actions.addAction((getShowErrorAction(throwable)))
    }

    private fun getArticles() {
        getArticlesUseCase(selectedCategory)
            .concatMapSingle { articles ->
                getArticlesReadUseCase().map { articlesRead ->
                    getArticleViewObjects(articles, articlesRead)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onGetArticlesSucceed,::onGetArticlesError)
            .addDisposable()
    }

    private fun getArticleViewObjects(
        articles: List<Article>,
        articlesRead: List<String>
    ): List<ArticleViewObject> {
        return articles.map { article ->
            article.toViewObject(isArticleRead(articlesRead, article.id))
        }
    }

    private fun onGetArticlesSucceed(articleViewObjects: List<ArticleViewObject>) {
        if(articleViewObjects.isNotEmpty()) hideLoading(isFromSwipeToRefresh = false)

        articleViewObjects.sortedByDescending { it.date }.run {
            _state.setState { state ->
                state?.copy(
                    articles = this,
                    isEmptyState = isEmpty() && !state.isLoading
                )
            }
        }
    }

    private fun onGetArticlesError(throwable: Throwable) {
        _state.setState { it?.copy(isEmptyState = hasNoArticlesBeingShown()) }
        _actions.addAction((getShowErrorAction(throwable)))
    }

    private fun getShowErrorAction(throwable: Throwable): ArticlesAction.ShowError {
        val errorMsg = when(throwable) {
            NetworkConnectionException -> R.string.network_error
            ServiceUnavailableException, NotFoundException -> R.string.service_unavailable_error
            SqliteException -> R.string.database_error
            else -> R.string.generic_error
        }

        val errorActionMsg = if(throwable.hasRetryAction()) {
            R.string.retry
        } else {
            null
        }

        return ArticlesAction.ShowError(errorMsg, errorActionMsg)
    }

    private fun Article.toViewObject(isArticleRead: Boolean): ArticleViewObject {
        return ArticleViewObject(
            id = id,
            title = title,
            author = author,
            date = date,
            imageUrl = imageUrl,
            isArticleRead
        )
    }

    private fun showLoading(isFromSwipeToRefresh: Boolean) {
        if(isFromSwipeToRefresh.not() && hasNoArticlesBeingShown()) {
            _state.setState { it?.copy(isLoading = true, isEmptyState = false) }
        }
    }

    private fun hideLoading(isFromSwipeToRefresh: Boolean) {
        if (isFromSwipeToRefresh) {
            _actions.addAction((ArticlesAction.StopSwipeRefresh))
        } else {
            _state.setState { it?.copy(isLoading = false) }
        }
    }

    private fun isArticleRead(articlesRead: List<String>, id: String): Boolean {
        return articlesRead.any { it == id }
    }

    private fun hasNoArticlesBeingShown(): Boolean {
        return _state.value?.articles.isNullOrEmpty()
    }

    private fun Throwable.hasRetryAction(): Boolean {
        return this !is ServiceUnavailableException && this !is NotFoundException
    }
}