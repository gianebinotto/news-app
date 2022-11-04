package com.olibra.news.presentation.common.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.format(): String {
    val format = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    return format.format(this)
}