package com.pengxh.kt.lite.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Message
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.convertColor
import com.pengxh.kt.lite.extensions.dp2px
import com.pengxh.kt.lite.extensions.sp2px
import com.pengxh.kt.lite.utils.WeakReferenceHandler

/**
 * 圆形进度条
 */
class CircleProgressBar constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundColor: Int
    private val foregroundColor: Int
    private var text: String = ""
    private var centerX = 0f
    private var centerY = 0f
    private lateinit var backgroundPaint: Paint
    private lateinit var foregroundPaint: Paint
    private lateinit var textPaint: TextPaint
    private var radius = 0

    //当前污染物测量值
    private var currentValue: String = ""

    //当前测量值转为弧度扫过的角度
    private var sweepAngle = 0f
    private val weakReferenceHandler: WeakReferenceHandler

    init {
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0)
        backgroundColor = a.getColor(
            R.styleable.CircleProgressBar_cpb_backgroundColor,
            Color.parseColor("#D3D3D3")
        )
        foregroundColor = a.getColor(
            R.styleable.CircleProgressBar_cpb_foregroundColor, R.color.blue.convertColor(
                context
            )
        )
        text = a.getString(R.styleable.CircleProgressBar_cpb_text).toString()
        a.recycle()
        //初始化画笔
        initPaint()
        weakReferenceHandler = WeakReferenceHandler { msg: Message ->
            if (msg.what == 2022010101) {
                sweepAngle = msg.arg1.toFloat() * 360 / 100
            }
            true
        }
    }

    private fun initPaint() {
        //背景色画笔
        backgroundPaint = Paint()
        backgroundPaint.color = backgroundColor
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.strokeWidth = 12f.dp2px(context).toFloat()
        backgroundPaint.strokeCap = Paint.Cap.ROUND //圆头
        backgroundPaint.isAntiAlias = true

        //前景色画笔
        foregroundPaint = Paint()
        foregroundPaint.color = foregroundColor
        foregroundPaint.style = Paint.Style.STROKE
        foregroundPaint.strokeWidth = 12f.dp2px(context).toFloat()
        foregroundPaint.strokeCap = Paint.Cap.ROUND //圆头
        foregroundPaint.isAntiAlias = true

        //文字画笔
        textPaint = TextPaint()
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.color = Color.parseColor("#333333")
        textPaint.textSize = 14f.sp2px(context).toFloat()
    }

    //计算出中心位置，便于定位
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = (w shr 1).toFloat()
        centerY = (h shr 1).toFloat()
    }

    //计算控件实际大小
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        // 获取宽
        val viewWidth: Int = if (widthSpecMode == MeasureSpec.EXACTLY) {
            // match_parent/精确值
            widthSpecSize
        } else {
            // wrap_content
            150f.dp2px(context)
        }
        // 获取高
        val viewHeight: Int = if (heightSpecMode == MeasureSpec.EXACTLY) {
            // match_parent/精确值
            heightSpecSize
        } else {
            // wrap_content
            150f.dp2px(context)
        }
        //园半径等于View宽或者高的一半
        radius = viewWidth - 20f.dp2px(context) shr 1
        setMeasuredDimension(viewWidth, viewHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /**
         * 画布移到中心位置
         */
        canvas.translate(centerX, centerY)
        //绘制进度条背景
        canvas.drawCircle(0f, 0f, radius.toFloat(), backgroundPaint)

        //绘制上面百分比
        val valueRect = Rect(0, 0, 0, 0)
        val valueY =
            (valueRect.centerY() + textPaint.fontMetrics.top * 0.3).toInt() //基线中间点的y轴计算公式
        if (TextUtils.isEmpty(currentValue)) {
            canvas.drawText("未定义!", valueRect.centerX().toFloat(), valueY.toFloat(), textPaint)
        } else {
            canvas.drawText(
                currentValue,
                valueRect.centerX().toFloat(),
                valueY.toFloat(),
                textPaint
            )
        }

        //绘制下面Tip文字
        val tipsRect = Rect(0, 0, 0, 0)
        //计算文字左下角坐标
        val tipsY = (tipsRect.centerY() - textPaint.fontMetrics.top * 1.2).toInt() //基线中间点的y轴计算公式
        if (TextUtils.isEmpty(text)) {
            canvas.drawText("未定义！", tipsRect.centerX().toFloat(), tipsY.toFloat(), textPaint)
        } else {
            canvas.drawText(text, tipsRect.centerX().toFloat(), tipsY.toFloat(), textPaint)
        }

        //绘制前景进度
        drawForegroundArc(canvas)
    }

    private fun drawForegroundArc(canvas: Canvas) {
        val rectF =
            RectF((-radius).toFloat(), (-radius).toFloat(), radius.toFloat(), radius.toFloat())
        canvas.drawArc(rectF, -90f, sweepAngle, false, foregroundPaint)
        invalidate()
    }

    fun setCurrentValue(value: Int) {
        currentValue = when {
            value < 0 -> {
                "0"
            }
            value > 100 -> {
                "100%"
            }
            else -> {
                "$value%"
            }
        }
        Thread {
            for (i in 0 until value) {
                val message = weakReferenceHandler.obtainMessage()
                message.arg1 = i
                message.what = 2022010101
                weakReferenceHandler.handleMessage(message)
                try {
                    Thread.sleep(10)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }
}