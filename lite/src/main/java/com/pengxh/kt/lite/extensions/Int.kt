package com.pengxh.kt.lite.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.content.ContextCompat
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * 小于10首位补〇
 * */
fun Int.appendZero(): String {
    return if (this < 10) {
        "0$this"
    } else {
        this.toString()
    }
}

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

private val decimalFormat = DecimalFormat("#")

/**
 * px转dp
 */
fun Int.px2dp(context: Context): Int {
    decimalFormat.roundingMode = RoundingMode.CEILING
    val result = decimalFormat.format(this / context.getScreenDensity())
    return result.toInt()
}

/**
 * dp转px
 */
fun Int.dp2px(context: Context): Int {
    decimalFormat.roundingMode = RoundingMode.CEILING
    val result = decimalFormat.format(this * context.getScreenDensity())
    return result.toInt()
}

/**
 * sp转px
 */
fun Int.sp2px(context: Context): Int {
    val floatValue = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this.toFloat(), context.resources.displayMetrics
    )
    decimalFormat.roundingMode = RoundingMode.CEILING
    val result = decimalFormat.format(floatValue)
    return result.toInt()
}