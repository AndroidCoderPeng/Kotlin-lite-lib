package com.pengxh.kt.lite.extensions

import android.content.Context
import androidx.core.content.ContextCompat

/**
 * 获取xml颜色值
 */
fun Int.convertColor(context: Context): Int {
    return ContextCompat.getColor(context, this)
}