package com.olibra.news.presentation.common.extensions

import androidx.lifecycle.MutableLiveData
import com.olibra.news.presentation.common.state.UIState

fun <T:UIState> MutableLiveData<T>.setState(state: (T?) -> T) {
    value = state(value)
}