package com.pengxh.kt.lite.extensions

import android.content.Context
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import com.pengxh.kt.lite.R

fun BarChart.init(context: Context, barLabels: MutableList<String>) {
    this.setNoDataText("无数据，无法渲染...")
    this.setNoDataTextColor(R.color.red)
    if (barLabels.isEmpty()) {
        this.clearValues()
        return
    }
    this.getPaint(Chart.PAINT_INFO).textSize = 14f.sp2px(context).toFloat()
    this.animateY(1200, Easing.EaseInOutQuad)
    this.setDrawGridBackground(false)
    this.setDrawBorders(false)
    this.setScaleEnabled(false)
    //去掉描述
    this.description.isEnabled = false
    //去掉图例
    this.legend.isEnabled = false
    val xAxis: XAxis = this.xAxis
    xAxis.textColor = R.color.lib_text_color.convertColor(context)
    xAxis.setDrawLabels(true) //绘制标签  指x轴上的对应数值
    xAxis.labelCount = barLabels.size // 设置x轴上的标签个数
    xAxis.valueFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return barLabels[value.toInt()]
        }
    }
    xAxis.setDrawAxisLine(true) //是否绘制轴线
    xAxis.setDrawGridLines(false) //设置x轴上每个点对应的线
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    xAxis.labelRotationAngle = -45f //X轴标签斜45度
    this.extraBottomOffset = 5f //解决X轴显示不完全问题
    //设置样式
    val rightAxis: YAxis = this.axisRight
    //设置图表右边的y轴禁用
    rightAxis.isEnabled = false
    val leftAxis: YAxis = this.axisLeft
    leftAxis.axisMinimum = 0f
}