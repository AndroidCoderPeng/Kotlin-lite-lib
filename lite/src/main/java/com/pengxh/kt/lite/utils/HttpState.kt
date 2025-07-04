package com.pengxh.kt.lite.utils

/**
 * 加载状态
 * sealed 关键字表示此类仅内部继承，密封类
 */
sealed class HttpState<out T> {
    /**
     * 默认状态
     */
    object Idle : HttpState<Nothing>()

    /**
     * 加载中
     */
    object Loading : HttpState<Nothing>()

    /**
     * 成功-带数据
     */
    data class Success<T>(val body: T) : HttpState<T>()

    /**
     * 成功-不带数据
     */
    object EmptySuccess : HttpState<Nothing>()

    /**
     * 失败
     */
    data class Error(
        val code: Int? = null,
        val message: String,
        val ex: Throwable? = null
    ) : HttpState<Nothing>()
}