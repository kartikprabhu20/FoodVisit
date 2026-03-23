package com.mintanable.core.common

sealed class LoadingStatus<out T> {
    data object Loading : LoadingStatus<Nothing>()
    data class Success<T>(val data: T) : LoadingStatus<T>()
    data class Error(val message: String, val cause: Throwable? = null) : LoadingStatus<Nothing>()
}
