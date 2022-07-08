package com.pengxh.kt.lite.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.core.widget.NestedScrollView

/**
 * 弹性NestedScrollView
 * */
class FlexibleScrollView(context: Context, attrs: AttributeSet) : NestedScrollView(context, attrs) {

    private lateinit var contentView: View
    private var startY = 0f
    private val originalRect: Rect = Rect() // 矩形，保存原始位置
    private var isScrolledOnTop = false
    private var isScrolledOnBottom = false
    private var isScrolled = false

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        /**
         * ScrollView唯一的一个子view的位置信息，这个位置信息在整个生命周期中保持不变
         * */
        originalRect.set(contentView.left, contentView.top, contentView.right, contentView.bottom)
    }

    /**
     * 在加载完xml后获取唯一的一个childView
     * */
    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 0) {
            /**
             * 获取第一个childView，也只能有一个View
             * */
            contentView = getChildAt(0)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                startY = ev.y
                /**
                 * 判断是否可以上拉或者下拉
                 * */
                isScrolledOnTop = isScrolledOnTop()
                isScrolledOnBottom = isScrolledOnBottom()
            }
            MotionEvent.ACTION_MOVE -> {
                /**
                 * 在移动过程既没有达到上拉的程度，又没有达到下拉的程度
                 * */
                if (!isScrolledOnTop && !isScrolledOnBottom) {
                    startY = ev.y
                    isScrolledOnTop = isScrolledOnTop()
                    isScrolledOnBottom = isScrolledOnBottom()
                }
                val distance = ev.y - startY
                //到达顶端
                if (isScrolledOnTop && distance > 0 || isScrolledOnBottom && distance < 0 || isScrolledOnTop && isScrolledOnBottom) {
                    /**
                     * 计算偏移量
                     * */
                    val offset = (distance * 0.5f).toInt()
                    contentView.layout(
                        originalRect.left, originalRect.top + offset,
                        originalRect.right, originalRect.bottom + offset
                    )
                    isScrolled = true
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isScrolled) {
                    //弹性动画恢复到原位置
                    val translateAnimation = TranslateAnimation(
                        0f, 0f,
                        contentView.top.toFloat(), originalRect.top.toFloat()
                    )
                    translateAnimation.duration = 300
                    contentView.startAnimation(translateAnimation)
                    contentView.layout(
                        originalRect.left, originalRect.top, originalRect.right, originalRect.bottom
                    )
                    // 将标志位重置
                    isScrolledOnTop = false
                    isScrolledOnBottom = false
                    isScrolled = false
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 判断是否滚动到顶部
     * */
    private fun isScrolledOnTop(): Boolean {
        return scrollY == 0 || contentView.height < height + scrollY
    }

    /**
     * 判断是否滚动到底部
     * */
    private fun isScrolledOnBottom(): Boolean {
        return contentView.height <= scrollY + height
    }
}