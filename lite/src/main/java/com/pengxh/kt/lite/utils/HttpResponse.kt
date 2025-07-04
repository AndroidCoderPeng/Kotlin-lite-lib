package com.pengxh.kt.lite.utils

sealed class HttpResponse<T>(
    val body: T? = null, val message: String? = null, val state: LoadState = LoadState.Idle
) {
    class Loading<T> : HttpResponse<T>(state = LoadState.Loading)

    class Success<T>(body: T) : HttpResponse<T>(body, state = LoadState.Success)

    class Error<T>(message: String, body: T? = null) :
        HttpResponse<T>(body, message, state = LoadState.Fail)
}