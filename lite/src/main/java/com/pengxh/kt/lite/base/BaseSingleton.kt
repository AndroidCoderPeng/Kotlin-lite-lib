package com.pengxh.kt.lite.base

/**
 * Double Check Singleton
 * */
abstract class BaseSingleton<in P, out T> {
    @Volatile
    private var instance: T? = null

    protected abstract val creator: (P) -> T

    fun obtainInstance(param: P): T = instance ?: synchronized(this) {
        instance ?: creator(param).also { instance = it }
    }
}