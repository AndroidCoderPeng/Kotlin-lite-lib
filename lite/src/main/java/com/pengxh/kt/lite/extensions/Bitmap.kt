package com.pengxh.kt.lite.extensions

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * 旋转图片
 */
fun Bitmap.rotateImage(angle: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle.toFloat())
    // 创建新的图片
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

/**
 * 获取图片base64编码
 */
fun Bitmap.toBase64(): String {
    try {
        val outputStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) //压缩质量
        outputStream.flush()
        outputStream.close()
        val bitmapBytes = outputStream.toByteArray()
        val result = Base64.encodeToString(
            bitmapBytes,
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
        result.replace("-", "+")
            .replace("_", "/")
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return ""
}