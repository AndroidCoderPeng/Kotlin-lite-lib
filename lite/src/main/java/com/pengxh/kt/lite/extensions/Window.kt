package com.pengxh.kt.lite.extensions

import android.view.Window
import android.view.WindowManager

fun Window.setScreenBrightness(brightness: Float) {
    val params = this.attributes as WindowManager.LayoutParams
    params.screenBrightness = brightness
    this.attributes = params
}