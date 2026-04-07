package com.pengxh.kt.lite.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import com.pengxh.kt.lite.extensions.sp2px
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
class OverlayCreator(builder: Builder) {

    class Builder {
        lateinit var context: Context
        lateinit var originalBitmap: Bitmap
        lateinit var lines: List<String>
        lateinit var position: Pair<Float, Float>
        lateinit var output: String
        lateinit var listener: OnStateChangedListener

        fun setContext(context: Context): Builder {
            this.context = context
            return this
        }

        /**
         * 设置原始Bitmap
         * */
        fun setOriginalBitmap(bitmap: Bitmap): Builder {
            this.originalBitmap = bitmap
            return this
        }

        /**
         * 设置水印
         * */
        fun setOverlay(lines: List<String>): Builder {
            this.lines = lines
            return this
        }

        /**
         * 设置水印文字位置
         * @param position 水印位置
         * */
        fun setPosition(position: Pair<Float, Float>): Builder {
            this.position = position
            return this
        }

        /**
         * 设置水印图片保存路径
         * */
        fun setOutputPath(output: String): Builder {
            this.output = output
            return this
        }

        /**
         * 设置水印图片回调监听
         * */
        fun setOnStateChangedListener(listener: OnStateChangedListener): Builder {
            this.listener = listener
            return this
        }

        fun build(): OverlayCreator {
            if (!::originalBitmap.isInitialized || !::lines.isInitialized || !::output.isInitialized || !::listener.isInitialized) {
                throw IllegalStateException("All properties must be initialized before building.")
            }
            return OverlayCreator(this)
        }
    }

    private val kTag = "OverlayCreator"
    private val context = builder.context
    private val originalBitmap = builder.originalBitmap
    private val lines = builder.lines
    private val position = builder.position
    private val output = builder.output
    private val listener = builder.listener
    private val textPaint = Paint().apply {
        color = Color.RED
        textSize = 30f.sp2px(context)
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
        isDither = true
        isFilterBitmap = true
    }

    /**
     * 开始添加水印
     * */
    fun start() {
        val job = SupervisorJob()
        val scope = CoroutineScope(Dispatchers.Main + job)

        listener.onStart()

        //添加水印
        scope.launch(Dispatchers.IO) {
            try {
                val bitmapConfig = originalBitmap.config ?: Bitmap.Config.ARGB_8888
                val copyBitmap = originalBitmap.copy(bitmapConfig, true)
                val canvas = Canvas(copyBitmap)

                val x: Float
                val y: Float
                if (position.first in 0f..1f && position.second in 0f..1f) {
                    // 相对位置，根据原始图片宽高计算
                    val width = originalBitmap.width
                    val height = originalBitmap.height
                    x = width * position.first
                    y = height * position.second
                } else {
                    // 按绝对位置算
                    x = position.first
                    y = position.second
                }
                Log.d(kTag, "position: [$x, $y]")

                // 计算文本的总高度
                val fontMetrics = textPaint.fontMetrics
                val lineHeight = fontMetrics.bottom - fontMetrics.top
                val totalTextHeight = lineHeight * lines.size + 10 * (lines.size - 1)

                lines.forEachIndexed { index, text ->
                    var drawX = x
                    var drawY = y

                    // 根据x的位置判断水平对齐方式：x > 图片宽度一半则右对齐，否则左对齐
                    val isRightAligned = x > copyBitmap.width / 2f
                    if (isRightAligned) {
                        // 右对齐：x是右边坐标，需要减去文本宽度
                        val textWidth = textPaint.measureText(text)
                        drawX = x - textWidth
                    }

                    // 根据y的位置判断垂直对齐方式：y > 图片高度一半则底对齐，否则顶对齐
                    val isBottomAligned = y > copyBitmap.height / 2f

                    if (isBottomAligned) {
                        // 底对齐：y是底部坐标，如果是多行文本，需要调整整体位置
                        if (lines.size == 1) {
                            // 单行文本
                            drawY = y
                        } else {
                            // 多行文本，y是最后一行的底部坐标
                            drawY = y - totalTextHeight + (lineHeight + 10) * index + lineHeight
                        }
                    } else {
                        // 顶对齐：y是顶部坐标
                        drawY = y + (lineHeight + 10) * index
                    }

                    // 检查是否超出图片边界，如果超出则不绘制
                    if (drawX >= 0 && drawY >= 0 && drawX <= copyBitmap.width && drawY <= copyBitmap.height) {
                        canvas.drawText(text, drawX, drawY, textPaint)
                    }
                }

                //编码照片是耗时操作，需要在子线程或者协程里面
                val file = File(output)
                val fos = FileOutputStream(file)
                try {
                    /**
                     * 第一个参数如果是Bitmap.CompressFormat.PNG,那不管第二个值如何变化，图片大小都不会变化，不支持png图片的压缩
                     * 第二个参数是压缩比重，图片存储在磁盘上的大小会根据这个值变化。值越小存储在磁盘的图片文件越小
                     * */
                    copyBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos)
                    fos.flush()
                } finally {
                    fos.close()
                }
                withContext(Dispatchers.Main) {
                    listener.onSuccess(file)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    listener.onFailure(e)
                }
            }
        }
    }

    interface OnStateChangedListener {
        fun onStart()

        fun onSuccess(file: File)

        fun onFailure(e: Exception)
    }
}