package com.pengxh.kt.lite.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.pengxh.kt.lite.R
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * 方向盘控件
 * */
@Deprecated(
    "废弃，不建议使用，有性能问题",
    replaceWith = ReplaceWith("SteeringWheelView"),
    level = DeprecationLevel.WARNING
)
class SteeringWheelController constructor(context: Context, attrs: AttributeSet) :
    View(context, attrs), View.OnTouchListener {
    //画布中心x
    private var canvasCenterX = 0f

    //画布中心y
    private var canvasCenterY = 0f
    private val borderColor: Int

    //外圆直径
    private val outerCircleDiameter: Float

    //线条粗细
    private val borderStroke: Float

    //控制板背景Paint
    private val backgroundPaint: Paint

    //外圆Paint
    private val outerCirclePaint: Paint

    //内圆Paint
    private val innerCirclePaint: Paint

    //中间开关Paint
    private val centerSwitchPaint: Paint

    //箭头Paint
    private lateinit var leftDirectionPaint: Paint
    private lateinit var topDirectionPaint: Paint
    private lateinit var rightDirectionPaint: Paint
    private lateinit var bottomDirectionPaint: Paint

    //箭头Path
    private lateinit var leftDirectionPath: Path
    private lateinit var topDirectionPath: Path
    private lateinit var rightDirectionPath: Path
    private lateinit var bottomDirectionPath: Path

    // 中间图标画图区域
    private lateinit var centerSwitchOval: RectF

    //外圆区域
    private lateinit var outerCircleRectF: RectF

    // 各控件使用状态
    private var leftTurn = false
    private var topTurn = false
    private var rightTurn = false
    private var bottomTurn = false
    private var centerTurn = false

    init {
        val type = context.obtainStyledAttributes(attrs, R.styleable.SteeringWheelController)
        val viewBackground = type.getColor(
            R.styleable.SteeringWheelController_controller_backgroundColor, Color.BLACK
        )
        borderColor = type.getColor(
            R.styleable.SteeringWheelController_controller_borderColor, Color.RED
        )
        outerCircleDiameter = type.getDimension(
            R.styleable.SteeringWheelController_controller_outerCircleDiameter, 200f
        )
        borderStroke = type.getDimension(
            R.styleable.SteeringWheelController_controller_borderStroke, 5f
        )
        type.recycle()

        backgroundPaint = Paint()
        backgroundPaint.isAntiAlias = true
        backgroundPaint.isDither = true
        backgroundPaint.style = Paint.Style.FILL
        backgroundPaint.color = viewBackground

        outerCirclePaint = Paint()
        outerCirclePaint.isAntiAlias = true
        outerCirclePaint.isDither = true
        outerCirclePaint.style = Paint.Style.STROKE
        outerCirclePaint.strokeWidth = borderStroke
        outerCirclePaint.color = borderColor

        innerCirclePaint = Paint()
        innerCirclePaint.isAntiAlias = true
        innerCirclePaint.isDither = true
        innerCirclePaint.style = Paint.Style.STROKE
        innerCirclePaint.strokeWidth = borderStroke
        innerCirclePaint.strokeCap = Paint.Cap.ROUND
        innerCirclePaint.color = borderColor

        centerSwitchPaint = Paint()
        centerSwitchPaint.isAntiAlias = true
        centerSwitchPaint.isDither = true
        centerSwitchPaint.style = Paint.Style.STROKE
        centerSwitchPaint.strokeWidth = borderStroke
        centerSwitchPaint.strokeCap = Paint.Cap.ROUND
        centerSwitchPaint.color = borderColor

        initDirection()

        //设置控件可触摸
        setOnTouchListener(this)
    }

    //箭头
    private fun initDirection() {
        leftDirectionPaint = Paint()
        leftDirectionPaint.isAntiAlias = true
        leftDirectionPaint.isDither = true
        leftDirectionPaint.style = Paint.Style.STROKE
        leftDirectionPaint.strokeWidth = borderStroke
        leftDirectionPaint.strokeCap = Paint.Cap.ROUND
        leftDirectionPaint.color = borderColor
        //路径
        leftDirectionPath = Path()

        topDirectionPaint = Paint()
        topDirectionPaint.isAntiAlias = true
        topDirectionPaint.isDither = true
        topDirectionPaint.style = Paint.Style.STROKE
        topDirectionPaint.strokeWidth = borderStroke
        topDirectionPaint.strokeCap = Paint.Cap.ROUND
        topDirectionPaint.color = borderColor
        //路径
        topDirectionPath = Path()

        rightDirectionPaint = Paint()
        rightDirectionPaint.isAntiAlias = true
        rightDirectionPaint.isDither = true
        rightDirectionPaint.style = Paint.Style.STROKE
        rightDirectionPaint.strokeWidth = borderStroke
        rightDirectionPaint.strokeCap = Paint.Cap.ROUND
        rightDirectionPaint.color = borderColor
        //路径
        rightDirectionPath = Path()

        bottomDirectionPaint = Paint()
        bottomDirectionPaint.isAntiAlias = true
        bottomDirectionPaint.isDither = true
        bottomDirectionPaint.style = Paint.Style.STROKE
        bottomDirectionPaint.strokeWidth = borderStroke
        bottomDirectionPaint.strokeCap = Paint.Cap.ROUND
        bottomDirectionPaint.color = borderColor
        //路径
        bottomDirectionPath = Path()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasCenterX = (w shr 1).toFloat()
        canvasCenterY = (h shr 1).toFloat()
        centerSwitchOval = RectF(
            canvasCenterX - outerCircleDiameter / 12,
            canvasCenterY - outerCircleDiameter / 12,
            canvasCenterX + outerCircleDiameter / 12,
            canvasCenterY + outerCircleDiameter / 12
        )
        val outerCircleRadius = outerCircleDiameter.toInt() shr 1 //半径
        // 大外圈区域
        outerCircleRectF = RectF(
            canvasCenterX - outerCircleRadius - borderStroke,
            canvasCenterY - outerCircleRadius - borderStroke,
            canvasCenterX + outerCircleRadius + borderStroke,
            canvasCenterY + outerCircleRadius + borderStroke
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val minimumWidth = suggestedMinimumWidth
        val minimumHeight = suggestedMinimumHeight
        val width = measureWidth(minimumWidth, widthMeasureSpec)
        val height = measureHeight(minimumHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    private fun measureWidth(defaultWidth: Int, measureSpec: Int): Int {
        var width = defaultWidth
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        when (specMode) {
            MeasureSpec.AT_MOST -> width = (outerCircleDiameter + borderStroke * 2).toInt()
            MeasureSpec.EXACTLY -> width = specSize
            MeasureSpec.UNSPECIFIED -> width = defaultWidth.coerceAtLeast(specSize)
        }
        return width
    }

    private fun measureHeight(defaultHeight: Int, measureSpec: Int): Int {
        var height = defaultHeight
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        when (specMode) {
            MeasureSpec.AT_MOST -> height = (outerCircleDiameter + borderStroke * 2).toInt()
            MeasureSpec.EXACTLY -> height = specSize
            MeasureSpec.UNSPECIFIED -> height = defaultHeight.coerceAtLeast(specSize)
        }
        return height
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //背景
        val outerCircleRadius = outerCircleDiameter.toInt() shr 1 //半径
        canvas.drawCircle(
            canvasCenterX,
            canvasCenterY,
            (outerCircleRadius + borderStroke),
            backgroundPaint
        )

        //外圆圆圈，+1是为了完全覆盖背景色的边缘
        canvas.drawCircle(
            canvasCenterX,
            canvasCenterY,
            (outerCircleRadius + (borderStroke.toInt() shr 1) + 1).toFloat(),
            outerCirclePaint
        )

        // 箭头长度
        val directionLength = 25

        //左箭头
        val leftX = canvasCenterX - outerCircleRadius + directionLength
        leftDirectionPath.moveTo(leftX, canvasCenterY)
        leftDirectionPath.lineTo(leftX + directionLength, canvasCenterY - directionLength)
        leftDirectionPath.moveTo(leftX, canvasCenterY)
        leftDirectionPath.lineTo(leftX + directionLength, canvasCenterY + directionLength)
        canvas.drawPath(leftDirectionPath, leftDirectionPaint)

        // 上箭头
        val topY = canvasCenterY - outerCircleRadius + directionLength
        topDirectionPath.moveTo(canvasCenterX, topY)
        topDirectionPath.lineTo(canvasCenterX - directionLength, topY + directionLength)
        topDirectionPath.moveTo(canvasCenterX, topY)
        topDirectionPath.lineTo(canvasCenterX + directionLength, topY + directionLength)
        canvas.drawPath(topDirectionPath, topDirectionPaint)

        // 右箭头
        val rightX = canvasCenterX + outerCircleRadius - directionLength
        rightDirectionPath.moveTo(rightX, canvasCenterY)
        rightDirectionPath.lineTo(rightX - directionLength, canvasCenterY - directionLength)
        rightDirectionPath.moveTo(rightX, canvasCenterY)
        rightDirectionPath.lineTo(rightX - directionLength, canvasCenterY + directionLength)
        canvas.drawPath(rightDirectionPath, rightDirectionPaint)

        // 下箭头
        val bottomY = canvasCenterY + outerCircleRadius - directionLength
        bottomDirectionPath.moveTo(canvasCenterX, bottomY)
        bottomDirectionPath.lineTo(canvasCenterX - directionLength, bottomY - directionLength)
        bottomDirectionPath.moveTo(canvasCenterX, bottomY)
        bottomDirectionPath.lineTo(canvasCenterX + directionLength, bottomY - directionLength)
        canvas.drawPath(bottomDirectionPath, bottomDirectionPaint)

        //内圆圆圈
        canvas.drawCircle(
            canvasCenterX, canvasCenterY, outerCircleDiameter / 6, innerCirclePaint
        )

        //中间开关
        canvas.drawArc(
            centerSwitchOval, -50f, 280f, false, centerSwitchPaint
        )
        canvas.drawLine(
            canvasCenterX,
            canvasCenterY - outerCircleDiameter / 15 - 5,
            canvasCenterX,
            canvasCenterY - outerCircleDiameter / 15 + 15,
            centerSwitchPaint
        )

        //根据点击位置设置外圆环颜色
        if (leftTurn) {
            leftDirectionPaint.color = Color.WHITE
        } else {
            leftDirectionPaint.color = borderColor
        }

        if (topTurn) {
            topDirectionPaint.color = Color.WHITE
        } else {
            topDirectionPaint.color = borderColor
        }

        if (rightTurn) {
            rightDirectionPaint.color = Color.WHITE
        } else {
            rightDirectionPaint.color = borderColor
        }

        if (bottomTurn) {
            bottomDirectionPaint.color = Color.WHITE
        } else {
            bottomDirectionPaint.color = borderColor
        }

        if (centerTurn) {
            centerSwitchPaint.color = borderColor
        } else {
            centerSwitchPaint.color = Color.WHITE
        }

        postInvalidate()
    }

    private var listener: OnWheelTouchListener? = null

    interface OnWheelTouchListener {
        /**
         * 左
         */
        fun onLeftTurn()

        /**
         * 上
         */
        fun onTopTurn()

        /**
         * 右
         */
        fun onRightTurn()

        /**
         * 下
         */
        fun onBottomTurn()

        /**
         * 中间
         */
        fun onCenterTurn()

        /**
         * 松开
         */
        fun onActionTurnUp(dir: Direction)
    }

    fun setOnWheelTouchListener(listener: OnWheelTouchListener?) {
        this.listener = listener
    }

    enum class Direction {
        LEFT, TOP, RIGHT, BOTTOM, CENTER
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val x: Float = event.x
        val y: Float = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 计算角度
                val mc = atan2((y - canvasCenterY).toDouble(), (x - canvasCenterX).toDouble())
                val mk = 180 * mc / Math.PI

                /**
                 * |
                 * [-180,-90] |  [-90,0]
                 * |
                 * -------------------------
                 * |
                 * [180,90]   |  [90,0]
                 * |
                 */
                // 计算点击的距离，区分点击的是环还是中心位置
                val mj = sqrt(
                    (x - canvasCenterX).toDouble().pow(2.0)
                            + (y - canvasCenterY).toDouble().pow(2.0)
                )
                setDefaultValue()

                // 判断
                if (mj > outerCircleDiameter / 15 + 20) {
                    if (mk < -45 && mk > -180 + 45) {
                        topTurn = true
                        listener?.onTopTurn()
                    } else if (mk > -45 && mk < 45) {
                        rightTurn = true
                        listener?.onRightTurn()
                    } else if (mk > 45 && mk < 180 - 45) {
                        bottomTurn = true
                        listener?.onBottomTurn()
                    } else {
                        leftTurn = true
                        listener?.onLeftTurn()
                    }
                } else {
                    centerTurn = true
                    listener?.onCenterTurn()
                }
            }

            MotionEvent.ACTION_UP -> {
                if (leftTurn) {
                    leftTurn = false
                    listener?.onActionTurnUp(Direction.LEFT)
                } else if (topTurn) {
                    topTurn = false
                    listener?.onActionTurnUp(Direction.TOP)
                } else if (rightTurn) {
                    rightTurn = false
                    listener?.onActionTurnUp(Direction.RIGHT)
                } else if (bottomTurn) {
                    bottomTurn = false
                    listener?.onActionTurnUp(Direction.BOTTOM)
                } else {
                    centerTurn = false
                    listener?.onActionTurnUp(Direction.CENTER)
                }
            }
        }
        return true
    }

    //每次手指抬起都重置方向状态
    private fun setDefaultValue() {
        leftTurn = false
        topTurn = false
        rightTurn = false
        bottomTurn = false
        centerTurn = false
    }
}