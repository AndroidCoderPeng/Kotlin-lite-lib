package com.pengxh.kt.lite.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.databinding.WidgetViewTitleBarBinding

/**
 * 界面顶部标题栏
 * */
class TitleBarView(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private var binding = WidgetViewTitleBarBinding.inflate(
        LayoutInflater.from(context), this, true
    )

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
        val isSmallerTitle = type.getBoolean(R.styleable.TitleBarView_tbv_smaller_title, false)
        type.recycle()

        //左边图标
        if (isShowLeft) {
            binding.leftButton.setImageResource(leftImageRes)
            binding.leftButton.setOnClickListener {
                listener?.onLeftClick()
            }
        }

        //文字
        binding.titleView.text = title
        binding.titleView.textSize = if (isSmallerTitle) {
            16f
        } else {
            18f
        }
        binding.titleView.setTextColor(titleColor)

        //右边图标
        if (isShowRight) {
            binding.rightButton.setImageResource(rightImageRes)
            binding.rightButton.setOnClickListener {
                listener?.onRightClick()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //渲染背景
        canvas.drawColor(this.solidColor)
    }

    /**
     * 动态设置标题
     * */
    fun setTitle(title: String) {
        binding.titleView.text = title
        invalidate()
    }

    /**
     * 获取当前显示标题文字
     * */
    fun getTitle(): String {
        return binding.titleView.text.toString()
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