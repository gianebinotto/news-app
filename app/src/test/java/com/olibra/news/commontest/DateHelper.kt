package com.olibra.news.commontest

import java.text.SimpleDateFormat
import java.util.Date

fun getFixedDate(date: String = "11/01/2022"): Date {
    val format = SimpleDateFormat("MM/dd/yyyy")
    return format.parse(date) ?: Date()
}