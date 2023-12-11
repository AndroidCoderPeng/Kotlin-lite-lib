package com.pengxh.kt.lite.base

/**
 * 双重锁单例抽象类
 * */
abstract class BaseSingleton<in C, out T> {
    @Volatile
    private var singleton: T? = null

    protected abstract val creator: (C) -> T

    fun get(context: C): T = singleton ?: synchronized(this) {
        singleton ?: creator(context).also { singleton = it }
    }
}