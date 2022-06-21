package com.pengxh.kt.lite.extensions

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import androidx.annotation.StyleRes

fun Dialog.initDialogLayoutParams(ratio: Float) {
    val window: Window = this.window ?: return
    window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    window.decorView.setBackgroundColor(Color.TRANSPARENT)
    val params = window.attributes
    params.width = ((this.context.getScreenWidth() * ratio).toInt())
    params.height = WindowManager.LayoutParams.WRAP_CONTENT
    window.attributes = params
}

fun Dialog.resetParams(gravity: Int, @StyleRes resId: Int, ratio: Double) {
    val window = this.window ?: return
    window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    window.decorView.setBackgroundColor(Color.TRANSPARENT)
    window.setGravity(gravity)
    //设置Dialog出现的动画
    window.setWindowAnimations(resId)
    val params = window.attributes
    params.width = ((this.context.getScreenWidth() * ratio).toInt())
    params.height = WindowManager.LayoutParams.WRAP_CONTENT
    window.attributes = params
}