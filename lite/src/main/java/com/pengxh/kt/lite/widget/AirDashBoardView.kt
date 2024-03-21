package com.pengxh.kt.lite.widget

import android.content.Context
import android.graphics.BlurMaskFilter
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
import com.pengxh.kt.lite.extensions.sp2px

/**
 * 空气污染指数表盘，仿HUAWEI天气
 *
 *      binding.airDashBoardView
 *                 .setCenterText("优")
 *                 .setCurrentValue(255)
 */
class AirDashBoardView constructor(context: Context, attrs: AttributeSet) : View(context, attrs) {

    //View中心X坐标
    private var centerX = 0f

    //View中心Y坐标
    private var centerY = 0f

    //控件边长
    private val viewSideLength: Int
    private val viewRect: Rect
    private val ringRadius: Int
    private lateinit var guidePaint: Paint

    //表盘圆弧色
    private val background: Int
    private val foreground: Int
    private val ringStroke: Int
    private val ringRectF: RectF
    private lateinit var ringPaint: Paint

    //阈值
    private lateinit var thresholdPaint: TextPaint

    private val centerTextColor: Int
    private val centerTextSize: Int

    //当前污染物测量值
    private var currentValue = 0

    //污染物最小值
    private var minValue = 0

    //污染物最大值
    private var maxValue = 500

    //当前测量值转为弧度扫过的角度
    private var sweepAngle = 0f

    //表盘中心文字
    private var centerText = "###"
    private lateinit var centerPaint: TextPaint
    private lateinit var forePaint: Paint

    init {
        val type = context.obtainStyledAttributes(attrs, R.styleable.AirDashBoardView)
        /**
         * getDimension()返回的是float
         * getDimensionPixelSize()返回的是实际数值的四舍五入
         * getDimensionPixelOffset返回的是实际数值去掉后面的小数点
         */
        ringRadius = type.getDimensionPixelSize(
            R.styleable.AirDashBoardView_air_ring_radius, 100.dp2px(context)
        )
        viewSideLength = ringRadius + 15.dp2px(context)
        //辅助框
        viewRect = Rect(-viewSideLength, -viewSideLength, viewSideLength, viewSideLength)
        ringRectF = RectF(
            -ringRadius.toFloat(), -ringRadius.toFloat(), ringRadius.toFloat(), ringRadius.toFloat()
        )
        background = type.getColor(R.styleable.AirDashBoardView_air_ring_background, Color.LTGRAY)
        foreground = type.getColor(R.styleable.AirDashBoardView_air_ring_foreground, Color.BLUE)
        ringStroke = type.getDimensionPixelSize(
            R.styleable.AirDashBoardView_air_ring_stroke, 5.dp2px(context)
        )

        centerTextSize = type.getDimensionPixelSize(
            R.styleable.AirDashBoardView_air_center_text_size, 20.sp2px(context)
        )
        centerTextColor = type.getColor(
            R.styleable.AirDashBoardView_air_center_text_color, Color.BLUE
        )
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

        ringPaint = Paint()
        ringPaint.color = background
        ringPaint.strokeCap = Paint.Cap.ROUND
        ringPaint.style = Paint.Style.STROKE
        ringPaint.strokeWidth = ringStroke.toFloat()
        ringPaint.isAntiAlias = true

        thresholdPaint = TextPaint()
        thresholdPaint.color = Color.DKGRAY
        thresholdPaint.isAntiAlias = true
        thresholdPaint.textAlign = Paint.Align.CENTER
        thresholdPaint.textSize = 16f.dp2px(context)

        centerPaint = TextPaint()
        centerPaint.color = centerTextColor
        centerPaint.isAntiAlias = true
        centerPaint.textAlign = Paint.Align.CENTER
        centerPaint.textSize = centerTextSize.toFloat()

        forePaint = Paint()
        forePaint.color = foreground
        forePaint.strokeCap = Paint.Cap.ROUND
        forePaint.style = Paint.Style.STROKE
        forePaint.strokeWidth = ringStroke.toFloat()
        forePaint.isAntiAlias = true
        //设置背景光晕
        forePaint.maskFilter = BlurMaskFilter(15f, BlurMaskFilter.Blur.SOLID)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = (w shr 1).toFloat()
        centerY = (h shr 1).toFloat()
    }

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
         * 画布移到中心位置，方便绘制一系列图形
         */
        canvas.translate(centerX, centerY)
//        drawGuides(canvas)

        /**
         * 从左往右画，顺时针，左边是180度
         */
        canvas.drawArc(ringRectF, 135f, 270f, false, ringPaint)

        /**
         * 绘制左边最小值
         */
        drawMinValue(canvas)
        /**
         * 绘制右边最大值
         */
        drawMaxValue(canvas)
        /**
         * 绘制中间实际值
         */
        drawCurrentValue(canvas)
        /**
         * 绘制中间文字
         */
        drawCenterText(canvas)
        /**
         *
         * 绘制前景进度
         */
        drawForegroundArc(canvas)
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

        //对角线
        canvas.drawLine(
            -viewSideLength.toFloat(),
            -viewSideLength.toFloat(),
            viewSideLength.toFloat(),
            viewSideLength.toFloat(),
            guidePaint
        )

        //对角线
        canvas.drawLine(
            viewSideLength.toFloat(),
            -viewSideLength.toFloat(),
            -viewSideLength.toFloat(),
            viewSideLength.toFloat(),
            guidePaint
        )

        //最小值基准线
        canvas.drawLine(
            -viewSideLength / 2f,
            -viewSideLength.toFloat(),
            -viewSideLength / 2f,
            viewSideLength.toFloat(),
            guidePaint
        )

        //最大值基准线
        canvas.drawLine(
            viewSideLength / 2f,
            -viewSideLength.toFloat(),
            viewSideLength / 2f,
            viewSideLength.toFloat(),
            guidePaint
        )
    }

    private fun drawForegroundArc(canvas: Canvas) {
        canvas.drawArc(ringRectF, 135f, sweepAngle, false, forePaint)
        invalidate()
    }

    private fun drawMinValue(canvas: Canvas) {
        canvas.drawText(
            minValue.toString(),
            -viewSideLength / 2f,
            viewSideLength / 2f + viewSideLength / 3f,
            thresholdPaint
        )
    }

    private fun drawMaxValue(canvas: Canvas) {
        canvas.drawText(
            maxValue.toString(),
            viewSideLength / 2f,
            viewSideLength / 2f + viewSideLength / 3f,
            thresholdPaint
        )
    }

    private fun drawCurrentValue(canvas: Canvas) {
        canvas.drawText(
            currentValue.toString(),
            0f,
            viewSideLength / 2f - viewSideLength / 3f,
            centerPaint
        )
    }

    private fun drawCenterText(canvas: Canvas) {
        canvas.drawText(
            centerText,
            0f,
            -viewSideLength / 12f,
            centerPaint
        )
    }

    fun setCenterText(centerText: String): AirDashBoardView {
        this.centerText = centerText
        invalidate()
        return this
    }

    fun setCurrentValue(value: Int) {
        currentValue = if (value < 0) {
            0
        } else if (value > maxValue) {
            maxValue
        } else {
            value
        }

        val i = intArrayOf(0)
        post(object : Runnable {
            override fun run() {
                i[0]++
                sweepAngle = i[0].toFloat() * 270 / maxValue
                invalidate()
                if (i[0] <= value) {
                    postDelayed(this, 5)
                } else {
                    removeCallbacks(this)
                }
            }
        })
    }
}