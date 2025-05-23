package com.pengxh.kt.lite.extensions

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

/**
 * 获取汉语拼音首字母
 * 如：汉语 ===> HY
 */
fun String.getHanYuPinyin(): String {
    val pinyinStr = StringBuilder()
    val newChar = this.toCharArray()
    val defaultFormat = HanyuPinyinOutputFormat()
    defaultFormat.caseType = HanyuPinyinCaseType.UPPERCASE
    defaultFormat.toneType = HanyuPinyinToneType.WITHOUT_TONE
    for (c in newChar) {
        if (c.code > 128) {
            try {
                pinyinStr.append(
                    PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)[0][0]
                )
            } catch (e: BadHanyuPinyinOutputFormatCombination) {
                e.printStackTrace()
            }
        } else {
            pinyinStr.append(c)
        }
    }
    return pinyinStr.toString()
}

/**
 * 手动换行
 * */
fun String.wrapLine(length: Int): String {
    // 单行最大显示15个汉字
    val step = if (length <= 0) {
        15
    } else {
        length
    }

    if (this.isBlank()) {
        return this
    }

    if (this.length <= step) {
        return this
    }

    val builder = StringBuilder()
    var start = 0
    while (start < this.length) {
        val end = minOf(start + step, this.length)
        builder.append(this.substring(start, end))
        if (end < this.length) {
            builder.append("\r\n")
        }
        start += step
    }

    return builder.toString()
}

/**
 * 时间转时间戳
 */
fun String.dateToTimestamp(): Long {
    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        val date = dateFormat.parse(this)!!
        return date.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return 0
}

/**
 * 判断是否已过时
 * */
fun String.isEarlierThenCurrent(): Boolean {
    val t1 = this.dateToTimestamp()
    val t2 = System.currentTimeMillis()
    return (t1 - t2) < 0
}

/**
 * 时间差-小时
 * */
fun String.diffCurrentTime(): Int {
    if (this.isBlank()) {
        return 0
    }
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    val date = simpleDateFormat.parse(this) ?: return 0
    val diff = abs(System.currentTimeMillis() - date.time)
    return (diff / (3600000)).toInt()
}

/**
 * yyyy-MM-dd HH:mm:ss 转 yyyy-MM-dd
 * */
fun String.formatToYearMonthDay(): String {
    if (this.isBlank()) {
        return this
    }
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    val date = simpleDateFormat.parse(this)!!

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    return dateFormat.format(date)
}


/**
 * 判断输入的是否是数字
 */
fun String.isNumber(): Boolean {
    val regex = Regex("([-+])?\\d+(\\.\\d+)?")
    return this.matches(regex)
}

/**
 * 判断输入的是否是数字和字母
 */
fun String.isLetterAndDigit(): Boolean {
    var isDigit = false
    var isLetter = false
    for (i in this.indices) {
        if (Character.isDigit(this[i])) {
            isDigit = true
        } else if (Character.isLetter(this[i])) {
            isLetter = true
        }
    }
    return isDigit && isLetter
}

/**
 * 判断是否为汉字
 */
fun String.isChinese(): Boolean {
    if (this.isNotEmpty()) {
        val regex = Regex("[\\u4e00-\\u9fa5]+")
        return this.matches(regex)
    }
    return false
}


fun String.isPhoneNumber(): Boolean {
    if (this.isBlank()) return false
    val regex = Regex("^1[3-9]\\d{9}\$")
    return this.length == 11 && this.matches(regex)
}

/**
 * 匹配邮箱地址
 */
fun String.isEmail(): Boolean {
    return if (this.isBlank()) {
        false
    } else {
        val regExp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"
        val regex = Regex(regExp)
        this.matches(regex)
    }
}

fun String.writeToFile(file: File) {
    try {
        val fileWriter = FileWriter(file, true)
        val writer = BufferedWriter(fileWriter)
        writer.write(this)
        writer.newLine() //换行
        writer.flush()
        writer.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun String.show(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

val gson by lazy { Gson() }

inline fun <reified T> unpackingResponse(response: String): T {
    return gson.fromJson(response, T::class.java)
}