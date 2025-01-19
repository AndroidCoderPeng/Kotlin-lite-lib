package com.pengxh.kt.lite.utils

import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class GalleryScaleHelper : RecyclerView.OnScrollListener() {

    private val kTag = "GalleryScaleHelper"
    private val snapHelper by lazy { LinearSnapHelper() }

    // 卡片的padding, 卡片间的距离等于2倍的pagePadding
    private val pagePadding = 15

    // 左边卡片显示大小
    private val leftCardShowWidth = 15

    // 卡片宽度
    private var cardWidth = 0
    private var currentItemOffset = 0

    //当前卡片的index
    private var currentItemPos = 0

    // 两边视图缩放比例
    private val scale = 0.9f

    fun attachToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                cardWidth = recyclerView.width - 2 * (pagePadding + leftCardShowWidth)
            }
        })

        recyclerView.addOnScrollListener(this)
        snapHelper.attachToRecyclerView(recyclerView)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dx != 0) {
            currentItemOffset += dx
            currentItemPos = currentItemOffset / cardWidth

            // 边界条件处理
            currentItemPos = max(
                0, min(currentItemPos, recyclerView.adapter?.itemCount?.minus(1) ?: 0)
            )

            val offset = currentItemOffset - currentItemPos * cardWidth
            val percent = max(abs(offset).toFloat() / cardWidth, 0.0001f)

            var leftView: View? = null
            var rightView: View? = null

            recyclerView.layoutManager?.apply {
                if (currentItemPos > 0) {
                    leftView = findViewByPosition(currentItemPos - 1)
                }
                val currentView = findViewByPosition(currentItemPos)
                recyclerView.adapter?.apply {
                    if (currentItemPos < itemCount - 1) {
                        rightView = findViewByPosition(currentItemPos + 1)
                    }
                }

                leftView?.apply {
                    scaleY = (1 - scale) * percent + scale
                }
                currentView?.apply {
                    scaleY = (scale - 1) * percent + 1
                }
                rightView?.apply {
                    scaleY = (1 - scale) * percent + scale
                }
            }
        }
    }

    fun removeOnScrollListener(recyclerView: RecyclerView) {
        recyclerView.removeOnScrollListener(this)
    }

    fun getCurrentIndex(): Int {
        return currentItemPos
    }
}