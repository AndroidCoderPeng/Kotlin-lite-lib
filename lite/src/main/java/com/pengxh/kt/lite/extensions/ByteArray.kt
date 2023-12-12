package com.pengxh.kt.lite.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
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

/**
 * ByteArray转Hex
 * */
fun ByteArray.toHex(): String {
    val hexArray = "0123456789ABCDEF".toCharArray()
    val hexChars = CharArray(this.size * 2)
    for (j in this.indices) {
        val v: Int = this[j].toInt() and 0xFF
        hexChars[j * 2] = hexArray[v ushr 4]
        hexChars[j * 2 + 1] = hexArray[v and 0x0F]
    }
    return String(hexChars)
}