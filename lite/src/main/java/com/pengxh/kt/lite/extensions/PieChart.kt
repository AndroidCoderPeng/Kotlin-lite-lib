package com.pengxh.kt.lite.extensions

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.pengxh.kt.lite.R

fun PieChart.init(context: Context) {
    this.setNoDataText("无数据，无法渲染...")
    this.setNoDataTextColor(R.color.red)
    this.getPaint(Chart.PAINT_INFO).textSize = 14f.sp2px(context).toFloat()
    this.setUsePercentValues(false) //百分比数字显示
    this.description.isEnabled = false
    this.dragDecelerationFrictionCoef = 0.95f //图表转动阻力摩擦系数[0,1]
    this.setBackgroundColor(Color.WHITE) //设置图表背景色
    this.rotationAngle = 0f
    this.isRotationEnabled = false
    this.isHighlightPerTapEnabled = true
    this.animateY(1200, Easing.EaseInOutQuad) // 设置图表展示动画效果
    this.setDrawEntryLabels(false) //不显示分类标签
    this.isDrawHoleEnabled = false //圆环显示
    this.setDrawCenterText(false) //圆环中心文字
    this.centerText = "分区用水占比图"
    this.setEntryLabelColor(R.color.blue.convertColor(context)) //图表文本字体颜色
    this.setEntryLabelTextSize(12f)
    //设置图表上下左右的偏移，类似于外边距，可以控制饼图大小
    this.setExtraOffsets(7.5f, 2.5f, 7.5f, 2.5f)
    //设置图例位置
    val legend = this.legend
    legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
    legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
    legend.orientation = Legend.LegendOrientation.HORIZONTAL
    //图例是否自动换行
    legend.isWordWrapEnabled = true
}