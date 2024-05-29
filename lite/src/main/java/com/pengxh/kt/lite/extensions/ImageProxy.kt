package com.pengxh.kt.lite.extensions

import android.graphics.Bitmap
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.pengxh.kt.lite.utils.FrameMetadata
import com.pengxh.kt.lite.utils.ImageProxyManager


/****以下方法针对小米/红米手机********************************************************************/

/**
 * Converts a YUV_420_888 image from CameraX API to a bitmap.
 */
@ExperimentalGetImage
fun ImageProxy.toBitmap(): Bitmap? {
    val frameMetadata = FrameMetadata.Builder()
        .setWidth(width)
        .setHeight(height)
        .setRotation(imageInfo.rotationDegrees)
        .build()
    image?.apply {
        val nv21Buffer = ImageProxyManager
            .yuv420ThreePlanesToNV21(this.planes, width, height) ?: return@apply
        return ImageProxyManager.getBitmap(nv21Buffer, frameMetadata);
    }
    return null
}