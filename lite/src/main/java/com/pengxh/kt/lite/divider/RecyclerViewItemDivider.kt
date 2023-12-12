package com.pengxh.kt.lite.divider

import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView

/**
 * 设置 [androidx.recyclerview.widget.RecyclerView] Item的分割线
 * */
class RecyclerViewItemDivider(strokeWidth: Int, color: Int) : RecyclerView.ItemDecoration() {

    private val dividerPaint by lazy { Paint() }

    init {
        dividerPaint.color = color
        dividerPaint.strokeWidth = strokeWidth.toFloat()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val view = parent.getChildAt(i)
            c.drawLine(
                0f,
                view.bottom.toFloat(),
                view.width.toFloat(),
                view.bottom.toFloat(),
                dividerPaint
            )
        }
    }
}