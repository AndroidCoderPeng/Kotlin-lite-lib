package com.pengxh.kt.lite.extensions

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.widget.ImageView

fun ImageView.switchBackground(blurBitmap: Bitmap?) {
    if (blurBitmap == null) return

    val lastDrawable: Drawable = when (this.drawable) {
        is TransitionDrawable -> (this.drawable as TransitionDrawable).getDrawable(1) ?: ColorDrawable(Color.TRANSPARENT)
        is BitmapDrawable -> this.drawable
        null -> ColorDrawable(Color.TRANSPARENT)
        else -> ColorDrawable(Color.TRANSPARENT)
    }

    val newDrawable = BitmapDrawable(resources, blurBitmap)

    val transitionDrawable = this.drawable as? TransitionDrawable ?: run {
        TransitionDrawable(arrayOf(lastDrawable, newDrawable)).apply {
            setIds()
            isCrossFadeEnabled = true
            setImageDrawable(this)
        }
    }

    transitionDrawable.setDrawableByLayerId(transitionDrawable.getId(1), newDrawable)
    transitionDrawable.startTransition(1000)
}

private fun TransitionDrawable.setIds() {
    setId(0, 0)
    setId(1, 1)
}