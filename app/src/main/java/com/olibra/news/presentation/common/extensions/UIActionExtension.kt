package com.olibra.news.presentation.common.extensions

import androidx.lifecycle.MutableLiveData
import com.olibra.news.presentation.common.action.UIAction

fun <T:UIAction> MutableLiveData<List<T>>.addAction(action: T) {
    value = value?.plus(action)
}

fun <T:UIAction> MutableLiveData<List<T>>.removeAction(actionId: String) {
    value = value?.filterNot { it.uniqueId == actionId }
}