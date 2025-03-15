package com.pengxh.kt.lite.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.text.TextPaint
import com.pengxh.kt.lite.annotations.WaterMarkPosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * 绘制水印
 */
class WaterMarkerEngine(builder: Builder) {

    private val textPaint by lazy { TextPaint() }
    private val textRect by lazy { Rect() }

    class Builder {
        lateinit var originalBitmap: Bitmap
        lateinit var marker: String
        var textColor = Color.WHITE
        var textSize = 16f
        var textMargin = 10f
        var position = WaterMarkPosition.RIGHT_BOTTOM
        lateinit var fileName: String
        lateinit var addedListener: OnWaterMarkerAddedListener

        /**
         * 设置原始Bitmap
         * */
        fun setOriginalBitmap(bitmap: Bitmap): Builder {
            this.originalBitmap = bitmap
            return this
        }

        /**
         * 设置水印文字
         * */
        fun setTextMaker(marker: String): Builder {
            this.marker = marker
            return this
        }

        /**
         * 设置水印文字颜色
         * */
        fun setTextColor(textColor: Int): Builder {
            this.textColor = textColor
            return this
        }

        /**
         * 设置水印文字大小
         * */
        fun setTextSize(textSize: Float): Builder {
            this.textSize = textSize
            return this
        }

        /**
         * 设置水印文字距离Bitmap内边距
         * */
        fun setTextMargin(textMargin: Float): Builder {
            this.textMargin = textMargin
            return this
        }

        /**
         * 设置水印文字位置
         * */
        fun setMarkerPosition(@WaterMarkPosition position: Int): Builder {
            this.position = position
            return this
        }

        /**
         * 设置水印图片保存路径
         * */
        fun setMarkedSavePath(fileName: String): Builder {
            this.fileName = fileName
            return this
        }

        /**
         * 设置水印图片回调监听
         * */
        fun setOnWaterMarkerAddedListener(addedListener: OnWaterMarkerAddedListener): Builder {
            this.addedListener = addedListener
            return this
        }

        fun build(): WaterMarkerEngine {
            if (!::originalBitmap.isInitialized || !::marker.isInitialized || !::fileName.isInitialized || !::addedListener.isInitialized) {
                throw IllegalStateException("All properties must be initialized before building.")
            }
            return WaterMarkerEngine(this)
        }
    }

    private val originalBitmap = builder.originalBitmap
    private val marker = builder.marker
    private val textColor = builder.textColor
    private val textSize = builder.textSize
    private val textMargin = builder.textMargin
    private val position = builder.position
    private val fileName = builder.fileName
    private val listener = builder.addedListener

    /**
     * 开始添加水印
     * */
    fun start() {
        val job = SupervisorJob()
        val scope = CoroutineScope(Dispatchers.Main + job)

        listener.onStart()
        //初始化画笔
        textPaint.color = textColor
        textPaint.typeface = Typeface.DEFAULT_BOLD
        textPaint.isDither = true // 获取清晰的图像采样
        textPaint.isFilterBitmap = true
        textPaint.textSize = textSize
        textPaint.getTextBounds(marker, 0, marker.length, textRect)

        //添加水印
        val bitmapConfig = originalBitmap.config!!
        val copyBitmap = originalBitmap.copy(bitmapConfig, true)
        scope.launch(Dispatchers.IO) {
            val canvas = Canvas(copyBitmap)
            val bitmapWidth = copyBitmap.width
            val bitmapHeight = copyBitmap.height

            when (position) {
                WaterMarkPosition.LEFT_TOP -> {
                    canvas.drawText(marker, textMargin, textMargin, textPaint)
                }

                WaterMarkPosition.RIGHT_TOP -> {
                    canvas.drawText(
                        marker, bitmapWidth - textRect.width() - textMargin, textMargin, textPaint
                    )
                }

                WaterMarkPosition.LEFT_BOTTOM -> {
                    canvas.drawText(marker, textMargin, bitmapHeight - textMargin, textPaint)
                }

                WaterMarkPosition.RIGHT_BOTTOM -> {
                    canvas.drawText(
                        marker,
                        bitmapWidth - textRect.width() - textMargin, bitmapHeight - textMargin,
                        textPaint
                    )
                }

                WaterMarkPosition.CENTER -> {
                    canvas.drawText(
                        marker,
                        (bitmapWidth - textRect.width()) / 2f, bitmapHeight / 2f,
                        textPaint
                    )
                }
            }

            //编码照片是耗时操作，需要在子线程或者协程里面
            val file = File(fileName)
            val fileOutputStream = FileOutputStream(file)
            /**
             * 第一个参数如果是Bitmap.CompressFormat.PNG,那不管第二个值如何变化，图片大小都不会变化，不支持png图片的压缩
             * 第二个参数是压缩比重，图片存储在磁盘上的大小会根据这个值变化。值越小存储在磁盘的图片文件越小
             * */
            copyBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            withContext(Dispatchers.Main) {
                listener.onMarkAdded(file)
            }
            job.cancel()
        }
    }

    interface OnWaterMarkerAddedListener {
        fun onStart()

        fun onMarkAdded(file: File)
    }
}