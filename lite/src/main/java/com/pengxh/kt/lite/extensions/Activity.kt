package com.pengxh.kt.lite.extensions

import androidx.activity.ComponentActivity
import androidx.viewbinding.ViewBinding

inline fun <reified VB : ViewBinding> ComponentActivity.binding() =
    lazy(LazyThreadSafetyMode.NONE) {
        inflateBinding<VB>(layoutInflater).also { setContentView(it.root) }
    }