package com.pengxh.kt.lite.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.min

/**
 * 保存图片，不压缩
 */
fun Bitmap.saveImage(imagePath: String) {
    try {
        val imageFile = File(imagePath)
        val fileOutputStream = FileOutputStream(imageFile)
        this.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

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
 *
 * 如果是上传到服务器，编码格式为：Base64.NO_WRAP
 *
 * 如果是本地使用，编码格式为：Base64.DEFAULT
 *
 * 默认：Base64.NO_WRAP
 */
fun Bitmap.toBase64(): String {
    try {
        val outputStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) //压缩质量
        val bitmapBytes = outputStream.toByteArray()
        outputStream.flush()
        outputStream.close()
        return Base64.encodeToString(bitmapBytes, Base64.NO_WRAP)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return ""
}

/**
 * 圆形图片
 *
 * 用[com.google.android.material.imageview.ShapeableImageView]代替
 * */
@Deprecated("用ShapeableImageView代替")
fun Bitmap.createRoundDrawable(context: Context, borderStroke: Float, color: Int): Bitmap {
    //转换为正方形后的宽高。以最短边为正方形边长，也是圆形图像的直径
    val squareBitmapBorderLength = min(this.width, this.height)

    val roundedBitmap = Bitmap.createBitmap(
        squareBitmapBorderLength, squareBitmapBorderLength, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(roundedBitmap)
    val paint = Paint()
    paint.isAntiAlias = true
    //清屏
    canvas.drawARGB(0, 0, 0, 0);
    //画圆角
    val rect = Rect(0, 0, squareBitmapBorderLength, squareBitmapBorderLength)
    val rectF = RectF(rect)
    canvas.drawRoundRect(
        rectF, squareBitmapBorderLength / 2f, squareBitmapBorderLength / 2f, paint
    )

    // 取两层绘制，显示上层
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, rect, rect, paint)

    val borderPaint = Paint()
    borderPaint.style = Paint.Style.STROKE
    borderPaint.isAntiAlias = true
    borderPaint.strokeWidth = borderStroke.dp2px(context)
    borderPaint.color = color

    //添加边框
    canvas.drawCircle(
        squareBitmapBorderLength / 2f,
        squareBitmapBorderLength / 2f,
        (squareBitmapBorderLength - borderStroke.dp2px(context)) / 2f,
        borderPaint
    )
    return roundedBitmap
}