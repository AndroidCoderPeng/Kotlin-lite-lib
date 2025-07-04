package com.pengxh.kt.lite.utils

/**
 * 加载状态
 * sealed 关键字表示此类仅内部继承，密封类
 */
sealed class HttpState<out T> {
    /**
     * 加载中
     */
    object Loading : HttpState<Nothing>()

    /**
     * 成功
     */
    data class Success<T>(val body: T) : HttpState<T>()

    /**
     * 失败
     */
    data class Error(
        val code: Int? = null,
        val message: String,
        val ex: Throwable? = null
    ) : HttpState<Nothing>()
}