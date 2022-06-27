package com.pengxh.kt.lite.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoScrollViewPager constructor(context: Context, attrs: AttributeSet) :
    ViewPager(context, attrs) {

    /**
     * dispatchTouchEvent一般情况不做处理，如果修改了默认的返回值,子孩子都无法收到事件
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 是否拦截
     * 拦截:会走到自己的onTouchEvent方法里面来
     * 不拦截:事件传递给ChildView
     */
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    /**
     * 是否消费事件
     * 消费:事件就结束
     * 不消费:往父控件传
     */
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }
}