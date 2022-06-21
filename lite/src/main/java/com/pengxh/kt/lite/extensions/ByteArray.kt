package com.pengxh.kt.lite.extensions

import android.graphics.*
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * Camera
 */
fun ByteArray.nv21ToBitmap(width: Int, height: Int): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        val image = YuvImage(this, ImageFormat.NV21, width, height, null)
        val outputStream = ByteArrayOutputStream()
        image.compressToJpeg(Rect(0, 0, width, height), 80, outputStream)
        val bmp = BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size())
        bitmap = bmp.rotateImage(-90)
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bitmap
}