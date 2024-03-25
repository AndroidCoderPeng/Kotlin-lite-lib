package com.pengxh.kt.lite.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import java.io.ByteArrayOutputStream


/**
 * CameraX 原始预览Image数据（imageProxy.format == ImageFormat.YUV_420_888）转Bitmap
 * CameraX 预览Image数据（imageProxy.format == ImageFormat.JPEG）转Bitmap
 */
fun Image.toBitmap(format: Int): Bitmap? {
    if (format == ImageFormat.JPEG) {
        val buffer = this.planes[0].buffer
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
    } else if (format == ImageFormat.YUV_420_888) {
        val yBuffer = this.planes[0].buffer
        val uBuffer = this.planes[1].buffer
        val vBuffer = this.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)

        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
    return null
}