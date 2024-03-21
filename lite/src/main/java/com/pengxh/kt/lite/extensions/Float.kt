package com.pengxh.kt.lite.extensions

import android.content.Context
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * px转dp
 */
fun Float.px2dp(context: Context): Float {
    return this / context.getScreenDensity()
}

/**
 * dp转px
 */
fun Float.dp2px(context: Context): Float {
    return this * context.getScreenDensity()
}

/**
 * sp转px
 */
fun Float.sp2px(context: Context): Float {
    val decimalFormat = DecimalFormat("#")
    decimalFormat.roundingMode = RoundingMode.CEILING
    val result = decimalFormat.format(this / context.getScreenDensity())
    return result.toFloat()
}