package com.pengxh.kt.lite.extensions

import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.viewbinding.ViewBinding

inline fun <reified VB : ViewBinding> ComponentActivity.binding() =
    lazy(LazyThreadSafetyMode.NONE) {
        inflateBinding<VB>(layoutInflater).also {
            setContentView(it.root)
        }
    }

inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater): VB {
    return VB::class.java
        .getMethod("inflate", LayoutInflater::class.java)
        .invoke(null, layoutInflater) as VB
}
