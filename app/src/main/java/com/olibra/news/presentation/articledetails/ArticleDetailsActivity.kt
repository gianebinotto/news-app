package com.olibra.news.presentation.articledetails

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.olibra.news.R
import com.olibra.news.databinding.ActivityArticleDetailsBinding
import com.olibra.news.presentation.common.extensions.load
import dagger.hilt.android.AndroidEntryPoint

private const val ARTICLE_ID = "articleId"
private const val TRANSITION_NAME = "transitionName"

@AndroidEntryPoint
class ArticleDetailsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityArticleDetailsBinding
    private val viewModel: ArticleDetailsViewModel by viewModels()

    private var isArticleRead = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArticleDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSharedElementTransition()
        setupActionBar()
        setupListeners()
        setupObservers()

        viewModel.onCreate(intent.extras?.getString(ARTICLE_ID))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.article_read_status, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.changeReadStatus -> {
                viewModel.onReadStatusChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.changeReadStatus)?.run {
            if(isArticleRead) {
                icon = ContextCompat.getDrawable(
                    this@ArticleDetailsActivity,
                    R.drawable.ic_article_read_2
                )
                title = getString(R.string.mark_article_as_unread_error)
            } else {
                icon = ContextCompat.getDrawable(
                    this@ArticleDetailsActivity,
                    R.drawable.ic_article_unread
                )
                title = getString(R.string.mark_article_as_read_error)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun setupSharedElementTransition() {
        val imageTransitionName: String? = intent.extras?.getString(TRANSITION_NAME)
        binding.articleImageView.transitionName = imageTransitionName
    }

    private fun setupActionBar() {
        setTitle(R.string.article_details_title)
        setSupportActionBar(binding.appBarLayout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.appBarLayout.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun setupListeners() {
        binding.readMoreButton.setOnClickListener { viewModel.onReadMoreClicked() }
    }

    private fun setupObservers() = with(viewModel) {
        state.observe(this@ArticleDetailsActivity) { it.setupArticleDetailsState() }
        actions.observe(this@ArticleDetailsActivity) { it.dispatchActions() }
    }

    private fun ArticleDetailsState.setupArticleDetailsState() {
        with(binding) {
            contentScrollView.isVisible = isEmptyState.not()
            emptyArticleTextView.isVisible = isEmptyState

            articleAuthorTextView.text = getString(R.string.article_author, author, date)
            articleTitleTextView.text = title
            articleContentTextView.text = content
            readMoreButton.isVisible = hasReadMore
            image?.let {
                articleImageView.load(this@ArticleDetailsActivity, it)
            }

            this@ArticleDetailsActivity.isArticleRead = isArticleRead
            invalidateOptionsMenu()
        }
    }

    private fun List<ArticleDetailsAction>.dispatchActions() {
        firstOrNull()?.let { articleDetailsAction ->
            when(articleDetailsAction) {
                is ArticleDetailsAction.ShowError ->
                    showError(
                        articleDetailsAction.errorMsg,
                        articleDetailsAction.errorActionMsg
                    )
                is ArticleDetailsAction.OpenReadMoreUrl -> openReadMoreUrl(articleDetailsAction.url)
            }
            viewModel.onActionConsumed(articleDetailsAction.uniqueId)
        }
    }

    private fun showError(
        @StringRes errorMsg: Int,
        @StringRes errorActionMsg: Int?
    ) {
        Snackbar.make(binding.root, errorMsg, Snackbar.LENGTH_LONG).run {
            errorActionMsg?.let {
                setAction(it) { viewModel.onRetryClicked(intent.extras?.getString(ARTICLE_ID)) }
            }
            show()
        }
    }

    private fun openReadMoreUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    companion object {
        fun getIntent(context: Context, articleId: String, transitionName: String?): Intent {
            return Intent(context, ArticleDetailsActivity::class.java).apply {
                putExtra(ARTICLE_ID, articleId)
                putExtra(TRANSITION_NAME, transitionName)
            }
        }
    }
}