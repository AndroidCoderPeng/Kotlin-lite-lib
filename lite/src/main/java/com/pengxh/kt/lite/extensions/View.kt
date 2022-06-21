package com.pengxh.kt.lite.extensions

import android.graphics.Bitmap
import android.view.View

/**
 * View转Bitmap
 */
fun View.toBitmap(): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        this.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        this.layout(0, 0, this.measuredWidth, this.measuredHeight)
        this.buildDrawingCache()
        bitmap = this.drawingCache
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return bitmap
}