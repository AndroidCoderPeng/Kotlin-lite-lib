package com.pengxh.kt.lite.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.dp2px
import com.pengxh.kt.lite.extensions.getScreenDensity
import java.math.RoundingMode
import java.text.DecimalFormat


/**
 * 界面顶部标题栏
 * */
class TitleBarView constructor(context: Context, attrs: AttributeSet) :
    RelativeLayout(context, attrs) {

    private val decimalFormat = DecimalFormat("#")

    init {
        val type = context.obtainStyledAttributes(attrs, R.styleable.TitleBarView)
        val leftImageRes = type.getResourceId(
            R.styleable.TitleBarView_tbv_left_image, R.drawable.ic_title_left
        )
        val isShowLeft = type.getBoolean(R.styleable.TitleBarView_tbv_show_left_image, true)
        val rightImageRes = type.getResourceId(
            R.styleable.TitleBarView_tbv_right_image, R.drawable.ic_title_right
        )
        val isShowRight = type.getBoolean(R.styleable.TitleBarView_tbv_show_right_image, false)
        val title = type.getText(R.styleable.TitleBarView_tbv_text)
        val titleColor = type.getColor(R.styleable.TitleBarView_tbv_text_color, Color.WHITE)
        val titleSize = type.getDimension(R.styleable.TitleBarView_tbv_text_size, 18f)
        type.recycle()

        //左边图标
        if (isShowLeft) {
            val leftImageParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            val leftImageView = ImageView(context)
            leftImageView.setImageResource(leftImageRes)
            leftImageView.setPadding(
                5.dp2px(context), 5.dp2px(context), 5.dp2px(context), 5.dp2px(context)
            )
            leftImageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            leftImageParams.addRule(CENTER_VERTICAL, TRUE)
            addView(leftImageView, leftImageParams)
            leftImageView.setOnClickListener {
                listener?.onLeftClick()
            }
        }

        //文字
        val titleParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        titleParams.height = 45.dp2px(context)
        val textView = TextView(context)
        textView.text = title
        textView.textSize = titleSize.sp2px()
        textView.gravity = Gravity.CENTER
        textView.setTextColor(titleColor)
        titleParams.addRule(CENTER_IN_PARENT, TRUE)
        textView.layoutParams = titleParams
        addView(textView)

        //右边图标
        if (isShowRight) {
            val rightImageParams = LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
            )
            val rightImageView = ImageView(context)
            rightImageView.setImageResource(rightImageRes)
            rightImageView.setPadding(
                5.dp2px(context), 5.dp2px(context), 5.dp2px(context), 5.dp2px(context)
            )
            rightImageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            rightImageParams.addRule(CENTER_VERTICAL, TRUE)
            rightImageParams.addRule(ALIGN_PARENT_END, TRUE)
            addView(rightImageView, rightImageParams)
            rightImageView.setOnClickListener {
                listener?.onRightClick()
            }
        }
    }

    private fun Float.sp2px(): Float {
        decimalFormat.roundingMode = RoundingMode.CEILING
        val result = decimalFormat.format(this / context.getScreenDensity())
        return result.toFloat()
    }

    /**
     * 设置View高度
     * */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(widthSpecSize, 45.dp2px(context))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //渲染背景
        canvas.drawColor(this.solidColor)
    }

    private var listener: OnClickListener? = null

    fun setOnClickListener(listener: OnClickListener?) {
        this.listener = listener
    }

    interface OnClickListener {
        fun onLeftClick()

        fun onRightClick()
    }
}