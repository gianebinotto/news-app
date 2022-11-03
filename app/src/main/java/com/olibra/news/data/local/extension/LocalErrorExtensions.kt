package com.olibra.news.data.local.extension

import android.database.sqlite.SQLiteException
import com.olibra.news.domain.model.ErrorException
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

fun <T> Flowable<T>.handleDatabaseException() = onErrorResumeNext { throwable: Throwable ->
    Flowable.error(getErrorException(throwable))
}

fun <T> Single<T>.handleDatabaseException() = onErrorResumeNext { throwable: Throwable ->
    Single.error(getErrorException(throwable))
}

fun Completable.handleDatabaseException() = onErrorResumeNext { throwable ->
    Completable.error(getErrorException(throwable))
}

private fun getErrorException(throwable: Throwable): ErrorException {
    return when (throwable) {
        is SQLiteException -> ErrorException.DatabaseErrorException.SqliteException
        else -> ErrorException.DatabaseErrorException.UnknownException
    }
}