package com.pengxh.kt.lite.utils

/**
 * 网络请求状态密封类（用于关注返回值的情况）
 */
sealed class HttpRequestState<out T> {
    /**
     * 加载中
     */
    object Loading : HttpRequestState<Nothing>()

    /**
     * 成功
     */
    data class Success<T>(val body: T) : HttpRequestState<T>()

    /**
     * 失败
     */
    data class Error(
        val code: Int? = null,
        val message: String,
        val ex: Throwable? = null
    ) : HttpRequestState<Nothing>()
}