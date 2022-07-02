package com.pengxh.kt.lite.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

/**
 * 获取xml颜色值
 */
fun Int.convertColor(context: Context): Int {
    return ContextCompat.getColor(context, this)
}

/**
 * res转Drawable
 * */
fun Int.convertDrawable(context: Context): Drawable? {
    return ContextCompat.getDrawable(context, this)
}