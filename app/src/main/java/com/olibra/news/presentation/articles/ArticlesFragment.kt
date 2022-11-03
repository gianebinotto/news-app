package com.olibra.news.presentation.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.olibra.news.databinding.FragmentArticlesBinding
import com.olibra.news.presentation.articledetails.ArticleDetailsActivity
import com.olibra.news.presentation.articles.listcomponent.ArticlesAdapter
import dagger.hilt.android.AndroidEntryPoint

private const val CATEGORY_MENU_ID = "category_menu_id"

@AndroidEntryPoint
class ArticlesFragment: Fragment() {

    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!

    private val adapter: ArticlesAdapter by lazy {
        ArticlesAdapter { articleId ->
            viewModel.onArticleClicked(articleId)
        }
    }

    private val viewModel: ArticlesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObservers()

        viewModel.onViewCreated(arguments?.getInt(CATEGORY_MENU_ID))
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        setupRecyclerView()
        setupSwipeRefresh()
    }

    private fun setupRecyclerView() {
        binding.articlesRecyclerView.adapter = adapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.onSwipeToRefreshArticles()
        }
    }

    private fun setupObservers() = with(viewModel) {
        state.observe(viewLifecycleOwner) { it.setupArticlesState() }
        actions.observe(viewLifecycleOwner) { it.dispatchActions() }
    }

    private fun ArticlesState.setupArticlesState() {
        with(binding) {
            articlesRecyclerView.isVisible = isEmptyState.not()
            circularProgressIndicator.isVisible = isLoading
            emptyArticlesTextView.isVisible = isEmptyState
        }

        if(articles.isNullOrEmpty().not()) adapter.submitList(articles)
    }

    private fun List<ArticlesAction>.dispatchActions() {
        firstOrNull()?.let { articleAction ->
            when(articleAction) {
                is ArticlesAction.NavigateToArticle -> navigateToArticle(articleAction.articleId)
                is ArticlesAction.ShowError -> showError(
                    articleAction.errorMsg,
                    articleAction.errorActionMsg
                )
                ArticlesAction.StopSwipeRefresh -> stopSwipeRefresh()
            }
            viewModel.onActionConsumed(articleAction.uniqueId)
        }
    }

    private fun navigateToArticle(articleId: String) {
        val intent = ArticleDetailsActivity.getIntent(requireContext(), articleId)
        startActivity(intent)
    }

    private fun stopSwipeRefresh() {
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun showError(
        @StringRes errorMsg: Int,
        @StringRes errorActionMsg: Int?
    ) {
        Snackbar.make(binding.root, errorMsg, Snackbar.LENGTH_LONG).run {
            errorActionMsg?.let {
                setAction(it) { viewModel.onRetryClicked() }
            }
            show()
        }
    }

    companion object {
        fun getInstance(@IdRes menuItemId: Int): ArticlesFragment {
            return ArticlesFragment().apply {
                arguments = Bundle().apply { putInt(CATEGORY_MENU_ID, menuItemId) }
            }
        }
    }
}