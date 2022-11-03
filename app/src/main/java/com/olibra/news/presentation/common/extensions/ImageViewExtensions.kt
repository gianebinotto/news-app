package com.olibra.news.presentation.common.extensions

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.olibra.news.R

fun ImageView.load(context: Context, url: String) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.article_loading_placeholder)
        .error(R.drawable.article_error_placeholder)
        .apply(RequestOptions.centerCropTransform())
        .into(this)
}