package com.pengxh.kt.lite.widget

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Handler
import android.os.Message
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.dp2px
import com.pengxh.kt.lite.utils.WeakReferenceHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 空气污染指数表盘，仿HUAWEI天气
 *
 *      binding.airDashBoardView
 *                 .setMaxValue(500)
 *                 .setCenterText("优")
 *                 .setAirRingForeground(Color.GREEN)
 *                 .setAirCenterTextColor(Color.RED)
 *                 .setAirCurrentValueColor(Color.BLUE)
 *                 .setCurrentValue(255)
 */
class AirDashBoardView constructor(context: Context, attrs: AttributeSet) : View(context, attrs),
    Handler.Callback {

    //View中心X坐标
    private var centerX = 0f

    //View中心Y坐标
    private var centerY = 0f

    //控件边长
    private val viewSideLength: Int
    private val viewRect: Rect
    private val ringRadius: Int
    private lateinit var guidePaint: Paint

    //表盘圆弧背景色
    private val background: Int
    private val ringStroke: Int
    private val ringRectF: RectF
    private lateinit var ringPaint: Paint

    //阈值
    private val thresholdTextSize: Int
    private val thresholdColor: Int
    private lateinit var thresholdPaint: TextPaint

    private val weakReferenceHandler by lazy { WeakReferenceHandler(this) }
    private val currentValueTextSize: Int
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
    private var centerText = "未定义"
    private lateinit var currentValuePaint: TextPaint
    private lateinit var centerPaint: TextPaint
    private lateinit var forePaint: Paint

    init {
        val type = context.obtainStyledAttributes(attrs, R.styleable.AirDashBoardView)
        /**
         * getDimension()返回的是float
         * getDimensionPixelSize()返回的是实际数值的四舍五入
         * getDimensionPixelOffset返回的是实际数值去掉后面的小数点
         */
        ringRadius = type.getDimensionPixelOffset(
            R.styleable.AirDashBoardView_air_ring_radius, 100.dp2px(context)
        )
        //需要给外围刻度留位置
        viewSideLength = ringRadius + 30.dp2px(context)
        //辅助框
        viewRect = Rect(-viewSideLength, -viewSideLength, viewSideLength, viewSideLength)
        ringRectF = RectF(
            -ringRadius.toFloat(), -ringRadius.toFloat(), ringRadius.toFloat(), ringRadius.toFloat()
        )
        background = type.getColor(R.styleable.AirDashBoardView_air_ring_background, Color.LTGRAY)
        ringStroke = type.getDimensionPixelOffset(
            R.styleable.AirDashBoardView_air_ring_stroke, 5.dp2px(context)
        )

        thresholdTextSize = type.getDimensionPixelOffset(
            R.styleable.AirDashBoardView_air_valueSize, 12.dp2px(context)
        )
        thresholdColor = type.getColor(R.styleable.AirDashBoardView_air_valueColor, Color.LTGRAY)

        currentValueTextSize = type.getDimensionPixelOffset(
            R.styleable.AirDashBoardView_air_current_valueSize, 24.dp2px(context)
        )
        centerTextSize = type.getDimensionPixelOffset(
            R.styleable.AirDashBoardView_air_center_textSize, 12.dp2px(context)
        )

        type.recycle()

        //初始化画笔
        initPaint()
    }

    private fun initPaint() {
        guidePaint = Paint()
        guidePaint.color = Color.LTGRAY
        guidePaint.style = Paint.Style.STROKE
        guidePaint.strokeWidth = 1f.dp2px(context).toFloat()
        guidePaint.isAntiAlias = true

        ringPaint = Paint()
        ringPaint.color = background
        ringPaint.strokeCap = Paint.Cap.ROUND
        ringPaint.style = Paint.Style.STROKE
        ringPaint.strokeWidth = ringStroke.toFloat().dp2px(context).toFloat()
        ringPaint.isAntiAlias = true
        //设置背景光晕
        ringPaint.maskFilter = BlurMaskFilter(15f, BlurMaskFilter.Blur.SOLID)

        thresholdPaint = TextPaint()
        thresholdPaint.color = thresholdColor
        thresholdPaint.isAntiAlias = true
        thresholdPaint.textAlign = Paint.Align.CENTER
        thresholdPaint.textSize = thresholdTextSize.toFloat()

        currentValuePaint = TextPaint()
        currentValuePaint.isAntiAlias = true
        currentValuePaint.textAlign = Paint.Align.CENTER
        currentValuePaint.textSize = currentValueTextSize.toFloat()

        centerPaint = TextPaint()
        centerPaint.isAntiAlias = true
        centerPaint.textAlign = Paint.Align.CENTER
        centerPaint.textSize = centerTextSize.toFloat()

        forePaint = Paint()
        forePaint.strokeCap = Paint.Cap.ROUND
        forePaint.style = Paint.Style.STROKE
        forePaint.strokeWidth = ringStroke.toFloat().dp2px(context).toFloat()
        forePaint.isAntiAlias = true
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
        val fontMetrics = thresholdPaint.fontMetrics
        val top = fontMetrics.top
        val bottom = fontMetrics.bottom
        canvas.drawText(
            minValue.toString(),
            -viewSideLength / 2f,
            viewSideLength / 2f - (top + bottom) * 2.5f,
            thresholdPaint
        )
    }

    private fun drawMaxValue(canvas: Canvas) {
        val fontMetrics = thresholdPaint.fontMetrics
        val top = fontMetrics.top
        val bottom = fontMetrics.bottom
        canvas.drawText(
            maxValue.toString(),
            viewSideLength / 2f,
            viewSideLength / 2f - (top + bottom) * 2.5f,
            thresholdPaint
        )
    }

    private fun drawCurrentValue(canvas: Canvas) {
        val fontMetrics = currentValuePaint.fontMetrics
        val top = fontMetrics.top
        val bottom = fontMetrics.bottom
        canvas.drawText(
            currentValue.toString(),
            0f,
            -(top + bottom) / 2f,
            currentValuePaint
        )
    }

    private fun drawCenterText(canvas: Canvas) {
        val fontMetrics = centerPaint.fontMetrics
        val top = fontMetrics.top
        val bottom = fontMetrics.bottom
        canvas.drawText(
            centerText,
            0f,
            (top + bottom) * 2,
            centerPaint
        )
    }

    fun setMaxValue(maxValue: Int): AirDashBoardView {
        this.maxValue = maxValue
        invalidate()
        return this
    }

    fun setCenterText(centerText: String): AirDashBoardView {
        this.centerText = centerText
        invalidate()
        return this
    }

    fun setAirRingForeground(@ColorInt color: Int): AirDashBoardView {
        forePaint.color = color
        invalidate()
        return this
    }

    fun setAirCenterTextColor(@ColorInt color: Int): AirDashBoardView {
        centerPaint.color = color
        invalidate()
        return this
    }

    fun setAirCurrentValueColor(@ColorInt color: Int): AirDashBoardView {
        currentValuePaint.color = color
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

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                for (i in 0 until currentValue) {
                    weakReferenceHandler.post(updateProgressRunnable.setProgress(i))
                    delay(10)
                }
            }
        }
    }

    private interface UpdateProgressRunnable : Runnable {
        fun setProgress(progress: Int): UpdateProgressRunnable
    }

    private val updateProgressRunnable = object : UpdateProgressRunnable {

        private var progress = 0

        override fun setProgress(progress: Int): UpdateProgressRunnable {
            this.progress = progress
            return this
        }

        override fun run() {
            sweepAngle = progress.toFloat() * 270 / maxValue
            invalidate()
        }
    }

    override fun handleMessage(msg: Message): Boolean {
        return true
    }
}