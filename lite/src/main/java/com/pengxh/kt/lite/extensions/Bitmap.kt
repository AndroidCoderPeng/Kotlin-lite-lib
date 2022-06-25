package com.pengxh.kt.lite.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.Base64
import android.view.Gravity
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
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

/**
 * 圆形图片
 * */
fun Bitmap.createRoundDrawable(context: Context, borderWidth: Int, @ColorInt color: Int): Drawable {
    //原图宽度
    val bitmapWidth: Int = this.width

    //原图高度
    val bitmapHeight: Int = this.height

    //转换为正方形后的宽高
    val bitmapSquareWidth = bitmapWidth.coerceAtMost(bitmapHeight)

    //最终图像的宽高
    val newBitmapSquareWidth = bitmapSquareWidth + borderWidth

    val roundedBitmap =
        Bitmap.createBitmap(newBitmapSquareWidth, newBitmapSquareWidth, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(roundedBitmap)
    val x = borderWidth + bitmapSquareWidth - bitmapWidth
    val y = borderWidth + bitmapSquareWidth - bitmapHeight

    //裁剪后图像,注意X,Y要除以2 来进行一个中心裁剪
    canvas.drawBitmap(this, (x shr 1).toFloat(), (y shr 1).toFloat(), null)

    val borderPaint = Paint()
    borderPaint.style = Paint.Style.STROKE
    borderPaint.isAntiAlias = true
    borderPaint.strokeWidth = borderWidth.toFloat()
    borderPaint.color = color

    //添加边框
    canvas.drawCircle(
        (canvas.width shr 1).toFloat(),
        (canvas.width shr 1).toFloat(),
        (newBitmapSquareWidth shr 1).toFloat(),
        borderPaint
    )

    val roundedBitmapDrawable =
        RoundedBitmapDrawableFactory.create(context.resources, roundedBitmap)
    roundedBitmapDrawable.gravity = Gravity.CENTER
    roundedBitmapDrawable.isCircular = true
    return roundedBitmapDrawable
}