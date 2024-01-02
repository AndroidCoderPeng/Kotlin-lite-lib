package com.pengxh.kt.lib.example

import android.content.BroadcastReceiver
import android.content.Context
import com.pengxh.kt.lite.base.BaseSingleton

class SingletonExample(private val context: Context) {
    companion object : BaseSingleton<Context, SingletonExample>() {
        override val creator: (Context) -> SingletonExample
            get() = ::SingletonExample
    }

    fun addAction(receiver: BroadcastReceiver?, action: String) {

    }

    fun destroy(vararg actions: String) {

    }
}