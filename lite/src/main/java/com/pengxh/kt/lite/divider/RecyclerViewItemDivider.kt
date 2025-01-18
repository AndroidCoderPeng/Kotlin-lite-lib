package com.pengxh.kt.lite.divider

import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView

/**
 * 设置 [androidx.recyclerview.widget.RecyclerView] Item的分割线
 * */
class RecyclerViewItemDivider(
    private val leftMargin: Float, private val rightMargin: Float, color: Int
) : RecyclerView.ItemDecoration() {

    private val dividerPaint by lazy { Paint() }

    init {
        dividerPaint.color = color
        dividerPaint.strokeWidth = 1f
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val view = parent.getChildAt(i)
            c.drawLine(
                leftMargin, view.bottom.toFloat(),
                view.width - rightMargin, view.bottom.toFloat(),
                dividerPaint
            )
        }
    }
}