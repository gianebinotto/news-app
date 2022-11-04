package com.olibra.news.data.repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.olibra.news.commontest.getFixedDate
import com.olibra.news.data.api.datasource.ArticleRemoteDataSourceImpl
import com.olibra.news.data.api.mapper.ArticleRemoteMapper
import com.olibra.news.data.api.service.ArticleService
import com.olibra.news.data.local.datasource.ArticleLocalDataSource
import com.olibra.news.domain.model.Article
import com.olibra.news.domain.model.ErrorException
import okhttp3.mockwebserver.MockWebServer
import com.olibra.news.domain.repository.ArticleRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import org.junit.Test
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class ArticlesRepositoryTest {

    private var localDataSource: ArticleLocalDataSource = mockk(relaxed = true)

    private val mockWebServer = MockWebServer()
    private val baseUrl = mockWebServer.url("/").toString()
    private val retrofit = createRetrofit()
    private val articleRepository = createRepository(retrofit)

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `updateArticles Should return article list When a request succeed`() {
        // Given
        val expectedArticles = listOf(
            Article(
                author = "John Doe",
                content = "content",
                date = getFixedDate("11/03/2022"),
                id = "1c4835b65b7f4141b8052b343d6cec75",
                imageUrl = "https://static.inshorts.com/inshorts/images/v1/variants/jpg/m/2022/11_nov/3_thu/img_1667462253435_358.jpg?",
                readMoreUrl = "https://www.google.com",
                title = "Article title",
                category = "technology"
            )
        )
        val mockResponse = MockResponse().setBody(
            this.javaClass.classLoader?.getResource("articlesSuccessResponse.json")?.readText().orEmpty()
        )

        every { localDataSource.addArticles(any(), "technology") } returns Completable.complete()
        mockWebServer.enqueue(mockResponse)

        // When
        val result = articleRepository.updateArticles("technology").test()

        // Then
        verify { localDataSource.addArticles(expectedArticles,"technology") }
        result.assertComplete()
    }

    @Test
    fun `updateArticles Should throw an UnknownException When a request fails with response code 500`() {
        // Given
        val mockResponse = MockResponse().setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        // When
        val result = articleRepository.updateArticles("all").test()

        // Then
        result.assertError(ErrorException.ApiErrorException.UnknownException::class.java)
    }

    @Test
    fun `updateArticles Should throw a NotFoundException When a request fails with response code 404`() {
        // Given
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        // When
        val result = articleRepository.updateArticles("all").test()

        // Then
        result.assertError(ErrorException.ApiErrorException.NotFoundException::class.java)
    }

    @Test
    fun `updateArticles Should throw a ServiceUnavailableException When a request fails with response code 503`() {
        // Given
        val mockResponse = MockResponse().setResponseCode(503)
        mockWebServer.enqueue(mockResponse)

        // When
        val result = articleRepository.updateArticles("all").test()

        // Then
        result.assertError(ErrorException.ApiErrorException.ServiceUnavailableException::class.java)
    }

    private fun createRepository(retrofit: Retrofit): ArticleRepository {
        val articleService = retrofit.create(ArticleService::class.java)
        val articleRemoteDataSource = ArticleRemoteDataSourceImpl(
            articleService,
            ArticleRemoteMapper()
        )
        return ArticleRepositoryImpl(articleRemoteDataSource, localDataSource)
    }

    private fun createRetrofit(): Retrofit {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(getConverterFactory())

        return retrofitBuilder.build()
    }

    @ExperimentalSerializationApi
    private fun getConverterFactory(): Converter.Factory {
        val contentType = MediaType.get("application/json")
        val json = Json {
            ignoreUnknownKeys = true
        }
        return json.asConverterFactory(contentType)
    }
}