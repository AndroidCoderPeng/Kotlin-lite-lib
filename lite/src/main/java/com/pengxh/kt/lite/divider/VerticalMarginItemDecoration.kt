package com.pengxh.kt.lite.divider

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 可以设置上下间距的RecyclerView分割线
 * */
class VerticalMarginItemDecoration(private val topMargin: Int, private val bottomMargin: Int) :
    RecyclerView.ItemDecoration() {

    //设置Item间的间隔
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = topMargin
        outRect.bottom = bottomMargin
    }
}