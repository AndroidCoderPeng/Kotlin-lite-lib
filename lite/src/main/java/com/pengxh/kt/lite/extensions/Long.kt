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
    if (date.toString().isBlank()) {
        return false
    }
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    try {
        return this < dateFormat.parse(date!!)!!.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return true
}

fun Long.formatFileSize(): String {
    val fileSize: String
    val df = DecimalFormat("0.00")
    df.roundingMode = RoundingMode.HALF_UP
    fileSize = when {
        this < 1024 -> {
            df.format(this) + "B"
        }

        this < 1048576 -> {
            df.format(this.toDouble() / 1024) + "K"
        }

        this < 1073741824 -> {
            df.format(this.toDouble() / 1048576) + "M"
        }

        else -> {
            df.format(this.toDouble() / 1073741824) + "G"
        }
    }
    return fileSize
}