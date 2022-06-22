package com.pengxh.kt.lite.extensions

import android.content.Context
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.pengxh.kt.lite.R

fun LineChart.init(context: Context) {
    this.setNoDataText("无数据，无法渲染...")
    this.setNoDataTextColor(R.color.red)
    this.getPaint(Chart.PAINT_INFO).textSize = 14f.sp2px(context).toFloat()
    this.setDrawGridBackground(false)
    this.setDrawBorders(false)
    this.animateY(1200, Easing.EaseInOutQuad)
    //设置样式
    val rightAxis: YAxis = this.axisRight
    //设置图表右边的y轴禁用
    rightAxis.isEnabled = false
    val leftAxis: YAxis = this.axisLeft
    leftAxis.axisMinimum = 0f
    this.isScaleXEnabled = true //X轴可缩放
    this.isScaleYEnabled = false //Y轴不可缩放
    //设置x轴
    val xAxis: XAxis = this.xAxis
    xAxis.textColor = R.color.lib_text_color.convertColor(context)
    xAxis.textSize = 10f
    xAxis.setLabelCount(7, true)
    xAxis.setDrawLabels(true) //绘制标签  指x轴上的对应数值
    xAxis.setDrawAxisLine(true) //是否绘制轴线
    xAxis.setDrawGridLines(false) //设置x轴上每个点对应的线
    xAxis.granularity = 1f //禁止放大后x轴标签重绘
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    this.extraBottomOffset = 5f //解决X轴显示不完全问题
    //去掉描述
    this.description.isEnabled = false
    //设置图例
    val legend = this.legend
    legend.orientation = Legend.LegendOrientation.HORIZONTAL
    legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
    legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
    //图例是否自动换行
    legend.isWordWrapEnabled = true
}