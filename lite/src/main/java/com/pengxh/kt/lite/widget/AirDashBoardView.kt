package com.pengxh.kt.lite.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Message
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.dp2px
import com.pengxh.kt.lite.utils.WeakReferenceHandler

/**
 * 空气污染指数表盘，仿HUAWEI天气
 */
class AirDashBoardView constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val weakReferenceHandler: WeakReferenceHandler
    private val currentValueTextSize: Int

    //表盘顶部文字
    private val topText: String
    private val topTextSize: Int
    private val topTextColor: Int
    private val valueTextSize: Int

    //阈值颜色
    private val valueColor: Int
    private val centerTextSize: Int

    //表盘圆弧背景色
    private val background: Int
    private val ringWidth: Int
    private val ringRadius: Int

    //当前污染物测量值
    private var currentValue = 0

    //污染物最小值
    private var minValue = 0

    //污染物最大值
    private var maxValue = 0

    //圆心x
    private var centerX = 0

    //圆心y
    private var centerY = 0

    //当前测量值转为弧度扫过的角度
    private var sweepAngle = 0f

    //表盘中心文字
    private var centerText: String

    private lateinit var valuePaint: TextPaint
    private lateinit var currentValuePaint: TextPaint
    private lateinit var topPaint: TextPaint
    private lateinit var centerPaint: TextPaint
    private lateinit var backPaint: Paint
    private lateinit var forePaint: Paint

    init {
        val type: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.AirDashBoardView, defStyleAttr, 0)
        /**
         * getDimension()返回的是float
         * getDimensionPixelSize()返回的是实际数值的四舍五入
         * getDimensionPixelOffset返回的是实际数值去掉后面的小数点
         */
        valueTextSize = type.getDimensionPixelOffset(
            R.styleable.AirDashBoardView_air_valueSize, 12f.dp2px(context)
        )
        valueColor =
            type.getColor(R.styleable.AirDashBoardView_air_valueColor, Color.parseColor("#F1F1F1"))
        currentValueTextSize = type.getDimensionPixelOffset(
            R.styleable.AirDashBoardView_air_current_valueSize, 24f.dp2px(context)
        )
        topText = type.getString(R.styleable.AirDashBoardView_air_top_text).toString()
        topTextSize = type.getDimensionPixelOffset(
            R.styleable.AirDashBoardView_air_top_textSize, 16f.dp2px(context)
        )
        topTextColor = type.getColor(
            R.styleable.AirDashBoardView_air_top_textColor,
            Color.parseColor("#FFFFFF")
        )
        centerText = type.getString(R.styleable.AirDashBoardView_air_center_text).toString()
        centerTextSize = type.getDimensionPixelOffset(
            R.styleable.AirDashBoardView_air_center_textSize, 12f.dp2px(context)
        )
        background = type.getColor(
            R.styleable.AirDashBoardView_air_ring_background,
            Color.parseColor("#F1F1F1")
        )
        ringWidth = type.getDimensionPixelOffset(
            R.styleable.AirDashBoardView_air_ring_width, 5f.dp2px(context)
        )
        ringRadius = type.getDimensionPixelOffset(
            R.styleable.AirDashBoardView_air_ring_radius, 100f.dp2px(context)
        )
        type.recycle()

        //初始化画笔
        initPaint()
        weakReferenceHandler = WeakReferenceHandler { msg ->
            if (msg.what == 2022061201) {
                sweepAngle = msg.arg1.toFloat() * 270 / maxValue
            }
            true
        }
    }

    private fun initPaint() {
        valuePaint = TextPaint()
        valuePaint.color = valueColor
        valuePaint.isAntiAlias = true
        valuePaint.textAlign = Paint.Align.CENTER
        valuePaint.textSize = valueTextSize.toFloat()

        currentValuePaint = TextPaint()
        currentValuePaint.isAntiAlias = true
        currentValuePaint.textAlign = Paint.Align.CENTER
        currentValuePaint.textSize = currentValueTextSize.toFloat()

        topPaint = TextPaint()
        topPaint.color = topTextColor
        topPaint.isAntiAlias = true
        topPaint.textAlign = Paint.Align.CENTER
        topPaint.textSize = topTextSize.toFloat()

        centerPaint = TextPaint()
        centerPaint.isAntiAlias = true
        centerPaint.textAlign = Paint.Align.CENTER
        centerPaint.textSize = centerTextSize.toFloat()

        backPaint = Paint()
        backPaint.color = background
        backPaint.strokeCap = Paint.Cap.ROUND
        backPaint.style = Paint.Style.STROKE
        backPaint.strokeWidth = ringWidth.toFloat().dp2px(context).toFloat()
        backPaint.isAntiAlias = true
        //设置背景光晕
        backPaint.maskFilter = BlurMaskFilter(15f, BlurMaskFilter.Blur.SOLID)

        forePaint = Paint()
        forePaint.strokeCap = Paint.Cap.ROUND
        forePaint.style = Paint.Style.STROKE
        forePaint.strokeWidth = ringWidth.toFloat().dp2px(context).toFloat()
        forePaint.isAntiAlias = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //圆心位置
        centerX = w shr 1
        centerY = h shr 1
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
            (ringRadius * 1.2).toFloat().dp2px(context)
        }
        // 获取高
        val mHeight: Int = if (heightSpecMode == MeasureSpec.EXACTLY) {
            // match_parent/精确值
            heightSpecSize
        } else {
            // wrap_content，外边界高
            (ringRadius * 1.2).toFloat().dp2px(context)
        }
        // 设置该view的宽高
        setMeasuredDimension(mWidth, mHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /**
         * 画布移到中心位置
         */
        canvas.translate(centerX.toFloat(), centerY.toFloat())
        /**
         * 画矩形  以两个点来画，起点和终点，通常是左上为起点，右下为终点  以下面这个图来看
         * 参数一：起点的Y轴坐标
         * 参数二：起点的X轴坐标
         * 参数三：终点的Y轴坐标
         * 参数四：终点的Y轴坐标
         * *
         * *  top
         * ****************
         * *          *
         * left *          *  right
         * *          *
         * *          *
         * ******************
         * bottom  *
         * *
         */
        drawBackgroundArc(canvas)
        /**
         * 绘制顶部文字
         */
        drawTopText(canvas)
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

    private fun drawBackgroundArc(canvas: Canvas) {
        /**
         * 从左往右画，顺时针，左边是180度
         */
        val rectF = RectF(
            -ringRadius.toFloat(), -ringRadius.toFloat(), ringRadius.toFloat(), ringRadius.toFloat()
        )
        canvas.drawArc(rectF, 135f, 270f, false, backPaint)
    }

    private fun drawForegroundArc(canvas: Canvas) {
        val rectF = RectF(
            -ringRadius.toFloat(), -ringRadius.toFloat(), ringRadius.toFloat(), ringRadius.toFloat()
        )
        canvas.drawArc(rectF, 135f, sweepAngle, false, forePaint)
        invalidate()
    }

    private fun drawTopText(canvas: Canvas) {
        val textRect = Rect(
            -ringRadius,
            -(2 * ringRadius + (ringRadius.toFloat() / 5).dp2px(context)),
            ringRadius,
            0
        )
        val fontMetrics: Paint.FontMetrics = topPaint.fontMetrics
        val top = fontMetrics.top //为基线到字体上边框的距离,即上图中的top
        val bottom = fontMetrics.bottom //为基线到字体下边框的距离,即上图中的bottom
        val baseLineY = (textRect.centerY() - top / 2 - bottom / 2).toInt() //基线中间点的y轴计算公式
        canvas.drawText(topText, textRect.centerX().toFloat(), baseLineY.toFloat(), topPaint)
    }

    private fun drawMinValue(canvas: Canvas) {
        val textRect = Rect(
            (-ringRadius * 1.25).toInt(),
            0,
            0,
            ringRadius + (ringRadius.toFloat() / 4).dp2px(context)
        )
        val fontMetrics: Paint.FontMetrics = valuePaint.fontMetrics
        val top = fontMetrics.top //为基线到字体上边框的距离,即上图中的top
        val bottom = fontMetrics.bottom //为基线到字体下边框的距离,即上图中的bottom
        val baseLineY = (textRect.centerY() - top / 2 - bottom / 2).toInt() //基线中间点的y轴计算公式
        canvas.drawText(
            minValue.toString(),
            textRect.centerX().toFloat(),
            baseLineY.toFloat(),
            valuePaint
        )
    }

    private fun drawMaxValue(canvas: Canvas) {
        val textRect = Rect(
            0,
            0,
            (ringRadius * 1.25).toInt(),
            ringRadius + (ringRadius.toFloat() / 4).dp2px(context)
        )
        val fontMetrics: Paint.FontMetrics = valuePaint.fontMetrics
        val top = fontMetrics.top //为基线到字体上边框的距离,即上图中的top
        val bottom = fontMetrics.bottom //为基线到字体下边框的距离,即上图中的bottom
        val baseLineY = (textRect.centerY() - top / 2 - bottom / 2).toInt() //基线中间点的y轴计算公式
        canvas.drawText(
            maxValue.toString(),
            textRect.centerX().toFloat(),
            baseLineY.toFloat(),
            valuePaint
        )
    }

    private fun drawCurrentValue(canvas: Canvas) {
        val textRect = Rect(0, 0, 0, 0)
        val fontMetrics: Paint.FontMetrics = currentValuePaint.fontMetrics
        val top = fontMetrics.top //为基线到字体上边框的距离,即上图中的top
        val bottom = fontMetrics.bottom //为基线到字体下边框的距离,即上图中的bottom
        val baseLineY = (textRect.centerY() - top / 2 - bottom / 2).toInt() //基线中间点的y轴计算公式
        canvas.drawText(
            currentValue.toString(),
            textRect.centerX().toFloat(),
            baseLineY.toFloat(),
            currentValuePaint
        )
    }

    private fun drawCenterText(canvas: Canvas) {
        val textRect = Rect(
            0, 0, 0, -(ringRadius.toFloat() / 7).dp2px(context)
        )
        val fontMetrics: Paint.FontMetrics = centerPaint.fontMetrics
        val top = fontMetrics.top //为基线到字体上边框的距离,即上图中的top
        val bottom = fontMetrics.bottom //为基线到字体下边框的距离,即上图中的bottom
        val baseLineY = (textRect.centerY() - top / 2 - bottom / 2).toInt() //基线中间点的y轴计算公式
        canvas.drawText(centerText, textRect.centerX().toFloat(), baseLineY.toFloat(), centerPaint)
    }

    /**
     * 获取xml颜色值
     */
    private fun getResourcesColor(res: Int): Int {
        return context.resources.getColor(res, null)
    }

    /** */
    fun setMinValue(minValue: Int) {
        this.minValue = minValue
    }

    fun setMaxValue(maxValue: Int) {
        this.maxValue = maxValue
    }

    fun setCurrentValue(value: Int) {
        currentValue = if (value < 0) {
            0
        } else value.coerceAtMost(500)

        Thread {
            for (i in 0 until currentValue) {
                val message: Message = weakReferenceHandler.obtainMessage()
                message.arg1 = i
                message.what = 2022061201
                weakReferenceHandler.handleMessage(message)
                try {
                    Thread.sleep(10)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    fun setCenterText(centerText: String) {
        this.centerText = centerText
    }

    fun setAirRingForeground(color: Int) {
        forePaint.color = color
    }

    fun setAirCenterTextColor(color: Int) {
        centerPaint.color = color
    }

    fun setAirCurrentValueColor(color: Int) {
        currentValuePaint.color = color
    }
}