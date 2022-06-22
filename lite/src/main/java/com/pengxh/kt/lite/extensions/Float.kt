package com.pengxh.kt.lite.extensions

import android.content.Context
import android.util.TypedValue

/**
 * px转dp
 */
fun Float.px2dp(context: Context): Int {
    val scale = context.obtainScreenDensity()
    return (this / scale + 0.5f).toInt()
}

/**
 * dp转px
 */
fun Float.dp2px(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        context.resources.displayMetrics
    ).toInt()
}

/**
 * sp转换成px
 */
fun Float.sp2px(context: Context): Int {
    val fontScale = context.obtainScreenDensity()
    return (this * fontScale + 0.5f).toInt()
}