package com.pengxh.kt.lite.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoScrollViewPager : ViewPager {
    private var noScroll = false

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)

    constructor(context: Context?) : super(context!!)

    fun setViewPagerNoScroll(noScroll: Boolean) {
        this.noScroll = noScroll
    }

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return if (noScroll) false else super.onInterceptTouchEvent(arg0)
    }
}