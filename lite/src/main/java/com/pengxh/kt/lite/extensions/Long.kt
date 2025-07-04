package com.pengxh.kt.lite.extensions

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * 判断时间是否在本月之内
 */
fun Long.isInCurrentMonth(): Boolean {
    //所选时间对应的月份
    val dateFormat = SimpleDateFormat("MM", Locale.CHINA)
    val selectedMonth = dateFormat.format(Date(this))
    //系统时间对应的月份
    val systemMonth = dateFormat.format(Date())
    return selectedMonth == systemMonth
}

/**
 * 时间戳转年月日时分秒
 */
fun Long.timestampToCompleteDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    return dateFormat.format(Date(this))
}

/**
 * 时间戳转年月日
 */
fun Long.timestampToDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    return dateFormat.format(Date(this))
}

/**
 * 时间戳转时分秒
 */
fun Long.timestampToTime(): String {
    val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.CHINA)
    return dateFormat.format(Date(this))
}

/**
 * 时间戳转分秒
 */
fun Long.millsToTime(): String {
    val dateFormat = SimpleDateFormat("mm:ss", Locale.CHINA)
    return dateFormat.format(Date(this))
}

/**
 * 根据时间戳得到上个月的日期
 */
fun Long.timestampToLastMonthDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    val calendar = Calendar.getInstance()
    calendar.time = Date(this)
    calendar[Calendar.DAY_OF_YEAR] = calendar[Calendar.DAY_OF_YEAR] - 29
    return dateFormat.format(calendar.time)
}

/**
 * 根据时间戳得到上周的日期
 */
fun Long.timestampToLastWeekDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    val calendar = Calendar.getInstance()
    calendar.time = Date(this)
    calendar[Calendar.DAY_OF_YEAR] = calendar[Calendar.DAY_OF_YEAR] - 6
    return dateFormat.format(calendar.time)
}

/**
 * 根据时间戳得到上周的时间
 */
fun Long.timestampToLastWeekTime(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    val calendar = Calendar.getInstance()
    calendar.time = Date(this)
    calendar[Calendar.DAY_OF_YEAR] = calendar[Calendar.DAY_OF_YEAR] - 6
    return dateFormat.format(calendar.time)
}

/**
 * 获取当前月份所在季度
 */
fun Long.getQuarterOfYear(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = Date(this)
    return calendar[Calendar.MONTH] / 3 + 1
}

/**
 * 判断时间是否早于当前时间
 */
fun Long.isEarlierThanStart(date: String?): Boolean {
    val dateString = date ?: return false
    if (dateString.isBlank()) {
        return false
    }
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    try {
        val parsedDate = dateFormat.parse(dateString) ?: return false
        return this < parsedDate.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return true
}

private const val KB = 1024L
private const val MB = 1024 * 1024L
private const val GB = 1024 * 1024 * 1024L
private val FILE_SIZE_DF = DecimalFormat("0.00").apply {
    roundingMode = RoundingMode.HALF_UP
}

fun Long.formatFileSize(): String {
    return when {
        this < 0 -> "Invalid size"
        this < KB -> "${FILE_SIZE_DF.format(this)} B"
        this < MB -> "${FILE_SIZE_DF.format(this.toDouble() / KB)} KB"
        this < GB -> "${FILE_SIZE_DF.format(this.toDouble() / MB)} MB"
        else -> "${FILE_SIZE_DF.format(this.toDouble() / GB)} GB"
    }
}