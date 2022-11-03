package com.olibra.news.presentation.articles

import androidx.annotation.IdRes
import com.olibra.news.R

enum class CategoryMenuIdRepresentation(
    @IdRes val menuItemId: Int,
    val category: String
) {
    ALL(R.id.allCategoryItem, "all"),
    AUTOMOBILE(R.id.automobileCategoryItem, "automobile"),
    BUSINESS(R.id.businessCategoryItem, "business"),
    ENTERTAINMENT(R.id.entertainmentCategoryItem, "entertainment"),
    MISCELLANEOUS(R.id.miscellaneousCategoryItem, "miscellaneous"),
    POLITICS(R.id.politicsCategoryItem, "politics"),
    SCIENCE(R.id.scienceCategoryItem, "science"),
    STARTUP(R.id.startupCategoryItem, "startup"),
    TECHNOLOGY(R.id.technologyCategoryItem, "technology"),
    WORLD(R.id.worldCategoryItem, "world");

    companion object {
        fun getCategoryByMenuId(
            @IdRes menuItemId: Int?
        ): CategoryMenuIdRepresentation? {
            return values().firstOrNull { it.menuItemId == menuItemId }
        }
    }
}