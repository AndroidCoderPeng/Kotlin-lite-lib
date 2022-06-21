package com.pengxh.kt.lite.utils

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.ColorInt

object ImmerseStatusBarUtil {
    fun setColor(activity: Activity, @ColorInt color: Int) {
        //限制android系统的版本
        // 设置状态栏透明
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // 生成一个状态栏大小的矩形
        val statusView = createStatusView(activity, color)
        // 添加 statusView 到布局中
        val decorView = activity.window.decorView as ViewGroup
        decorView.addView(statusView)
        // 设置根布局的参数
        val rootView =
            (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        rootView.fitsSystemWindows = true
        rootView.clipToPadding = true
    }

    /**
     * 生成一个和状态栏大小相同的矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @return 状态栏矩形条
     */
    private fun createStatusView(activity: Activity, @ColorInt color: Int): View {
        // 获得状态栏高度
        val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight = activity.resources.getDimensionPixelSize(resourceId)
        // 绘制一个和状态栏一样高的矩形
        val statusView = View(activity)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight)
        statusView.layoutParams = params
        statusView.setBackgroundColor(color)
        return statusView
    }
}