package com.olibra.news.domain.model

sealed class ErrorException : Exception() {

    sealed class ApiErrorException : ErrorException() {
        object NetworkConnectionException : ApiErrorException()
        object ServiceUnavailableException : ApiErrorException()
        object NotFoundException : ApiErrorException()
        object UnknownException : ApiErrorException()
    }

    sealed class DatabaseErrorException : ErrorException() {
        object SqliteException : DatabaseErrorException()
        object UnknownException : DatabaseErrorException()
    }
}