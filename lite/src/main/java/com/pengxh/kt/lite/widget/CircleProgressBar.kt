package com.pengxh.kt.lite.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.dp2px

/**
 * 圆形进度条
 */
class CircleProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val ringRadius: Int
    private var rectF: RectF

    //控件边长
    private val viewSideLength: Int
    private val ringStroke: Int
    private val viewRect: Rect
    private lateinit var guidePaint: Paint

    private val backgroundColor: Int
    private val foregroundColor: Int
    private val textColor: Int
    private var text: String = ""
    private var textSize = 18f
    private var hideText = false

    private var centerX = 0f
    private var centerY = 0f
    private lateinit var backgroundPaint: Paint
    private lateinit var foregroundPaint: Paint
    private lateinit var textPaint: TextPaint

    //当前值
    private var currentValue: String = ""

    //当前测量值转为弧度扫过的角度
    private var sweepAngle = 0f

    init {
        val type = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar)
        ringRadius = type.getDimensionPixelSize(
            R.styleable.CircleProgressBar_cpb_ring_radius, 100.dp2px(context)
        )
        rectF = RectF(
            -ringRadius.toFloat(),
            -ringRadius.toFloat(),
            ringRadius.toFloat(),
            ringRadius.toFloat()
        )
        ringStroke = type.getDimensionPixelSize(
            R.styleable.CircleProgressBar_cpb_ring_stroke, 10.dp2px(context)
        )
        viewSideLength = ringRadius + 10.dp2px(context)
        //辅助框
        viewRect = Rect(-viewSideLength, -viewSideLength, viewSideLength, viewSideLength)

        backgroundColor = type.getColor(R.styleable.CircleProgressBar_cpb_background, Color.LTGRAY)
        foregroundColor = type.getColor(R.styleable.CircleProgressBar_cpb_foreground, Color.BLUE)
        text = type.getString(R.styleable.CircleProgressBar_cpb_text).toString()
        textColor = type.getColor(R.styleable.CircleProgressBar_cpb_text_color, Color.DKGRAY)
        textSize = type.getDimension(R.styleable.CircleProgressBar_cpb_text_size, 18f)
        hideText = type.getBoolean(R.styleable.CircleProgressBar_cpb_hide_text, false)
        type.recycle()
        //初始化画笔
        initPaint()
    }

    private fun initPaint() {
        guidePaint = Paint()
        guidePaint.color = Color.LTGRAY
        guidePaint.style = Paint.Style.STROKE
        guidePaint.strokeWidth = 1f.dp2px(context)
        guidePaint.isAntiAlias = true

        //背景色画笔
        backgroundPaint = Paint()
        backgroundPaint.color = backgroundColor
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.strokeWidth = ringStroke.toFloat()
        backgroundPaint.strokeCap = Paint.Cap.ROUND //圆头
        backgroundPaint.isAntiAlias = true

        //前景色画笔
        foregroundPaint = Paint()
        foregroundPaint.color = foregroundColor
        foregroundPaint.style = Paint.Style.STROKE
        foregroundPaint.strokeWidth = ringStroke.toFloat()
        foregroundPaint.strokeCap = Paint.Cap.ROUND //圆头
        foregroundPaint.isAntiAlias = true

        //文字画笔
        textPaint = TextPaint()
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.color = textColor
        textPaint.textSize = textSize
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
        val mWidth: Int = if (widthSpecMode == MeasureSpec.EXACTLY) {
            // match_parent/精确值
            widthSpecSize
        } else {
            // wrap_content，外边界宽
            (viewSideLength * 2)
        }
        // 获取高
        val mHeight: Int = if (heightSpecMode == MeasureSpec.EXACTLY) {
            // match_parent/精确值
            heightSpecSize
        } else {
            // wrap_content，外边界高
            (viewSideLength * 2)
        }
        // 设置该view的宽高
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /**
         * 画布移到中心位置
         */
        canvas.translate(centerX, centerY)
//        drawGuides(canvas)

        //绘制进度条背景
        canvas.drawCircle(0f, 0f, ringRadius.toFloat(), backgroundPaint)

        if (!hideText) {
            //绘制上面百分比
            val fontMetrics = textPaint.fontMetrics
            val top = fontMetrics.top
            val bottom = fontMetrics.bottom
            if (currentValue.isBlank()) {
                canvas.drawText("###", 0f, (top + bottom) / 2, textPaint)
            } else {
                canvas.drawText(currentValue, 0f, (top + bottom) / 2, textPaint)
            }

            //绘制下面Tip文字
            if (text.isBlank()) {
                canvas.drawText("###", 0f, -(top + bottom) * 1.5f, textPaint)
            } else {
                canvas.drawText(text, 0f, -(top + bottom) * 1.5f, textPaint)
            }
        }

        //绘制前景进度
        canvas.drawArc(rectF, -90f, sweepAngle, false, foregroundPaint)
    }

    /**
     * 辅助线
     * */
    private fun drawGuides(canvas: Canvas) {
        //最外层方框，即自定义View的边界
        canvas.drawRect(viewRect, guidePaint)

        //中心横线
        canvas.drawLine(
            -viewSideLength.toFloat(),
            0f,
            viewSideLength.toFloat(),
            0f,
            guidePaint
        )

        //中心竖线
        canvas.drawLine(
            0f,
            -viewSideLength.toFloat(),
            0f,
            viewSideLength.toFloat(),
            guidePaint
        )
    }

    fun setCurrentValue(value: Int) {
        currentValue = when {
            value < 0 -> "0"
            value > 100 -> "100%"
            else -> "$value%"
        }

        val i = intArrayOf(0)
        post(object : Runnable {
            override fun run() {
                i[0]++
                sweepAngle = i[0].toFloat() * 360 / 100
                invalidate()
                if (i[0] <= value) {
                    postDelayed(this, 10)
                } else {
                    removeCallbacks(this)
                }
            }
        })
    }
}