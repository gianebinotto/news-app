package com.olibra.news.presentation.common.viewmodel

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class DisposableViewModel : androidx.lifecycle.ViewModel() {

    private val disposable = CompositeDisposable()

    protected fun Disposable.addDisposable(): Disposable =
        apply { disposable.add(this) }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}