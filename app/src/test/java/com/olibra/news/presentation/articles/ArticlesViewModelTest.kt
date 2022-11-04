package com.olibra.news.presentation.articles

import com.olibra.news.domain.usecase.GetArticlesReadUseCase
import com.olibra.news.domain.usecase.GetArticlesUseCase
import com.olibra.news.domain.usecase.UpdateArticlesUseCase
import com.olibra.news.commontest.getOrAwaitValue
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.olibra.news.R
import com.olibra.news.commontest.ArticlesDataTest
import com.olibra.news.presentation.articles.ArticlesViewObjectDataTest.toViewObject
import com.olibra.news.commontest.LocalTestRule
import com.olibra.news.domain.model.ErrorException
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.After
import java.util.Calendar
import java.util.UUID

class ArticlesViewModelTest {

    @get:Rule
    val rule: LocalTestRule = LocalTestRule()

    private val getArticlesUseCase: GetArticlesUseCase = mockk(relaxed = true)
    private val getArticlesReadUseCase: GetArticlesReadUseCase = mockk(relaxed = true)
    private val updateArticlesUseCase: UpdateArticlesUseCase = mockk(relaxed = true)

    private lateinit var viewModel: ArticlesViewModel

    @Before
    fun setup() {
        viewModel = ArticlesViewModel(
            getArticlesUseCase,
            updateArticlesUseCase,
            getArticlesReadUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `onViewCreated Should call get articles with right category`() {
        // When
        viewModel.onViewCreated(R.id.allCategoryItem)

        // Then
        verify { getArticlesUseCase("all") }
    }

    @Test
    fun `onViewCreated Should update state with articles emitted When get articles succeeds`() {
        // Given
        val articlesEmitted = ArticlesDataTest.getArticles()
        val expectedState = ArticlesState(
            isLoading = false,
            articles = articlesEmitted.map { it.toViewObject(isRead = false) },
            isEmptyState = false
        )

        every { getArticlesUseCase("all") } returns Flowable.create({
            it.onNext(articlesEmitted)
        }, BackpressureStrategy.LATEST)
        every { getArticlesReadUseCase() } returns Single.just(emptyList())

        // When
        viewModel.onViewCreated(R.id.allCategoryItem)

        // Then
        assertEquals(expectedState, viewModel.state.getOrAwaitValue())
    }

    @Test
    fun `onViewCreated Should set article view object property as read state When article is returned in articles read`() {
        // Given
        val articlesEmitted = ArticlesDataTest.getArticles() + ArticlesDataTest.getArticle("10")
        val expectedState = ArticlesState(
            isLoading = false,
            articles = articlesEmitted.map { it.toViewObject(it.id == "10") },
            isEmptyState = false
        )

        every { getArticlesUseCase("all") } returns Flowable.create({
            it.onNext(articlesEmitted)
        }, BackpressureStrategy.LATEST)
        every { getArticlesReadUseCase() } returns Single.just(listOf("10"))

        // When
        viewModel.onViewCreated(R.id.allCategoryItem)

        // Then
        assertEquals(expectedState, viewModel.state.getOrAwaitValue())
    }

    @Test
    fun `onViewCreated Should sort articles by descending date When there are articles emitted`() {
        // Given
        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DATE, -1)
        }
        val article1 = ArticlesDataTest.getArticle("1").copy(date = yesterday.time)
        val article2 = ArticlesDataTest.getArticle("2").copy(date = Calendar.getInstance().time)
        val articlesEmitted = listOf(article1, article2)
        val expectedState = ArticlesState(
            isLoading = false,
            articles = listOf(article2.toViewObject(isRead = false), article1.toViewObject(isRead = false)),
            isEmptyState = false
        )

        every { getArticlesUseCase("all") } returns Flowable.create({
            it.onNext(articlesEmitted)
        }, BackpressureStrategy.LATEST)
        every { getArticlesReadUseCase() } returns Single.just(emptyList())

        // When
        viewModel.onViewCreated(R.id.allCategoryItem)

        // Then
        assertEquals(expectedState, viewModel.state.getOrAwaitValue())
    }

    @Test
    fun `onViewCreated Should set empty state When article list is empty and state isn't loading`() {
        // Given
        val expectedState = ArticlesState(
            isLoading = false,
            articles = listOf(),
            isEmptyState = true
        )

        every { getArticlesUseCase("all") } returns Flowable.create({
            it.onNext(listOf())
        }, BackpressureStrategy.LATEST)
        every { getArticlesReadUseCase() } returns Single.just(emptyList())

        // When
        viewModel.onViewCreated(R.id.allCategoryItem)

        // Then
        assertEquals(expectedState, viewModel.state.getOrAwaitValue())
    }

    @Test
    fun `onViewCreated Should set empty When get articles fails and there are empty articles list being shown`() {
        // Given
        val expectedState = ArticlesState(
            isLoading = false,
            articles = listOf(),
            isEmptyState = true
        )

        every { getArticlesUseCase("all") } returns Flowable.create({
            it.onNext(emptyList())
            it.tryOnError(ErrorException.DatabaseErrorException.SqliteException)
        }, BackpressureStrategy.LATEST)
        every { getArticlesReadUseCase() } returns Single.just(emptyList())

        // When
        viewModel.onViewCreated(R.id.allCategoryItem)

        // Then
        assertEquals(expectedState, viewModel.state.getOrAwaitValue())
    }

    @Test
    fun `onViewCreated Should set empty When get articles fails and there aren't articles state update`() {
        // Given
        val expectedState = ArticlesState(
            isLoading = false,
            articles = null,
            isEmptyState = true
        )

        every { getArticlesUseCase("all") } returns Flowable.create({
            it.tryOnError(ErrorException.DatabaseErrorException.SqliteException)
        }, BackpressureStrategy.LATEST)
        every { getArticlesReadUseCase() } returns Single.just(emptyList())

        // When
        viewModel.onViewCreated(R.id.allCategoryItem)

        // Then
        assertEquals(expectedState, viewModel.state.getOrAwaitValue())
    }

    @Test
    fun `onViewCreated Should show error with right message When get articles fails and throw SqliteException`() {
        // Given
        val expectedActions = listOf(ArticlesAction.ShowError(
            R.string.database_error,
            R.string.retry
        ))

        every { getArticlesUseCase("all") } returns Flowable.create({
            it.tryOnError(ErrorException.DatabaseErrorException.SqliteException)
        }, BackpressureStrategy.LATEST)
        every { getArticlesReadUseCase() } returns Single.just(emptyList())

        // When
        viewModel.onViewCreated(R.id.allCategoryItem)

        // Then
        assertEquals(expectedActions, viewModel.actions.getOrAwaitValue())
    }

    @Test
    fun `onViewCreated Should show error with right message When get articles fails and throw UnknownException`() {
        // Given
        val expectedActions = listOf(ArticlesAction.ShowError(
            R.string.generic_error,
            R.string.retry
        ))

        every { getArticlesUseCase("all") } returns Flowable.create({
            it.tryOnError(ErrorException.DatabaseErrorException.UnknownException)
        }, BackpressureStrategy.LATEST)
        every { getArticlesReadUseCase() } returns Single.just(emptyList())

        // When
        viewModel.onViewCreated(R.id.allCategoryItem)

        // Then
        assertEquals(expectedActions, viewModel.actions.getOrAwaitValue())
    }

    @Test
    fun `onViewCreated Should call refresh articles with right category`() {
        // When
        viewModel.onViewCreated(R.id.allCategoryItem)

        // Then
        verify { updateArticlesUseCase("all") }
    }

    @Test
    fun `onViewCreated Should show error with right message When refresh articles fails and throw NetworkConnectionException`() {
        // Given
        val expectedActions = listOf(ArticlesAction.ShowError(
            R.string.network_error,
            R.string.retry
        ))

        every { updateArticlesUseCase("all") } returns Completable.error(ErrorException.ApiErrorException.NetworkConnectionException)

        // When
        viewModel.onViewCreated(R.id.allCategoryItem)

        // Then
        assertEquals(expectedActions, viewModel.actions.getOrAwaitValue())
    }

    @Test
    fun `onViewCreated Should show error with right message When refresh articles fails and throw ServiceUnavailableException`() {
        // Given
        val expectedActions = listOf(ArticlesAction.ShowError(
            R.string.service_unavailable_error,
            null
        ))

        every { updateArticlesUseCase("all") } returns Completable.error(ErrorException.ApiErrorException.ServiceUnavailableException)

        // When
        viewModel.onViewCreated(R.id.allCategoryItem)

        // Then
        assertEquals(expectedActions, viewModel.actions.getOrAwaitValue())
    }

    @Test
    fun `onViewCreated Should show error with right message When refresh articles fails and throw NotFoundException`() {
        // Given
        val expectedActions = listOf(ArticlesAction.ShowError(
            R.string.service_unavailable_error,
            null
        ))

        every { updateArticlesUseCase("all") } returns Completable.error(ErrorException.ApiErrorException.NotFoundException)

        // When
        viewModel.onViewCreated(R.id.allCategoryItem)

        // Then
        assertEquals(expectedActions, viewModel.actions.getOrAwaitValue())
    }

    @Test
    fun `onArticleClicked Should send action to open article details`() {
        // Given
        val articleId = "123"
        val expected = listOf(ArticlesAction.NavigateToArticle(articleId))

        // When
        viewModel.onArticleClicked(articleId)

        // Then
        assertEquals(expected, viewModel.actions.getOrAwaitValue())
    }

    @Test
    fun `onResume Should call get articles with right category`() {
        // Given
        viewModel.onViewCreated(R.id.allCategoryItem)

        // When
        viewModel.onResume()

        // Then
        verify { getArticlesUseCase("all") }
    }

    @Test
    fun `onActionConsumed Should remove action from action list`() {
        // Given
        mockkStatic(UUID::class)

        every { UUID.randomUUID().toString() } returns "id"
        viewModel.onArticleClicked("123")

        // When
        viewModel.onActionConsumed("id")

        // Then
        assertEquals(listOf<ArticlesAction>(), viewModel.actions.getOrAwaitValue())
    }

    @Test
    fun `onSwipeToRefreshArticles Should refresh articles with right category`() {
        // Given
        viewModel.onViewCreated(R.id.automobileCategoryItem)

        // When
        viewModel.onSwipeToRefreshArticles()

        // Then
        verify { updateArticlesUseCase("automobile") }
    }

    @Test
    fun `onRetryClicked Should refresh articles with right category`() {
        // Given
        viewModel.onViewCreated(R.id.automobileCategoryItem)

        // When
        viewModel.onRetryClicked()

        // Then
        verify { updateArticlesUseCase("automobile") }
    }
}