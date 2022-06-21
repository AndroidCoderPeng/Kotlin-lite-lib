package com.pengxh.kt.lite.extensions

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.callback.IDownloadListener
import okhttp3.*
import java.io.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * 时间转时间戳
 */
fun String.dateToTimestamp(): Long {
    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        val date: Date = dateFormat.parse(this)!!
        return date.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return 0
}

/**
 * 判断输入的是否是数字
 */
fun String.isNumber(): Boolean {
    var isDigit = false
    for (element in this) {
        isDigit = Character.isDigit(element)
    }
    return isDigit
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
        val pattern = Pattern.compile("[\\u4e00-\\u9fa5]+")
        return pattern.matcher(this).matches()
    }
    return false
}


fun String.isPhoneNumber(): Boolean {
    return if (this.length != 11) {
        false
    } else {
        val regExp =
            "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$"
        val pattern = Pattern.compile(regExp)
        pattern.matcher(this).matches()
    }
}

/**
 * 匹配邮箱地址
 */
fun String.isEmail(): Boolean {
    return if (this.isBlank()) {
        false
    } else {
        val regExp =
            "^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"
        val pattern = Pattern.compile(regExp)
        pattern.matcher(this).matches()
    }
}

/**
 * 过滤空格，回车
 */
//fun String.filterString(): String {
//    if (this.isBlank()) {
//        return this
//    }
//    //先过滤回车换行
//    val p = Pattern.compile("\\s*|\t|\r|\n")
//    val m = p.matcher(this)
//    s = m.replaceAll("")
//    //再过滤空格
//    return this.trim { it <= ' ' }.replace(" ", "")
//}

fun String.writeToFile(file: File?) {
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

fun String.downloadFile(downloadDir: String?, listener: IDownloadListener) {
    val httpClient = OkHttpClient()
    val request = Request.Builder().get().url(this).build()
    val newCall = httpClient.newCall(request)
    /**
     * 如果已被加入下载队列，则取消之前的，重新下载
     * 断点下载以后再考虑
     */
    if (newCall.isExecuted()) {
        newCall.cancel()
    }
    newCall.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            call.cancel()
            e.printStackTrace()
        }

        @Throws(IOException::class)
        override fun onResponse(call: Call, response: Response) {
            var stream: InputStream? = null
            val buf = ByteArray(2048)
            var len: Int
            var fos: FileOutputStream? = null
            try {
                val fileBody = response.body
                if (fileBody != null) {
                    stream = fileBody.byteStream()
                    val total = fileBody.contentLength()
                    listener.onDownloadStart(total)
                    val file = File(
                        downloadDir,
                        this@downloadFile.substring(this@downloadFile.lastIndexOf("/") + 1)
                    )
                    fos = FileOutputStream(file)
                    var current: Long = 0
                    while (stream.read(buf).also { len = it } != -1) {
                        fos.write(buf, 0, len)
                        current += len.toLong()
                        listener.onProgressChanged(current)
                    }
                    fos.flush()
                    listener.onDownloadEnd(file)
                }
            } catch (e: Exception) {
                call.cancel()
                e.printStackTrace()
            } finally {
                try {
                    stream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    fos!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    })
}

fun String.show(context: Context) {
    val toast = Toast(context)
    val textView = TextView(context)
    textView.setBackgroundResource(R.drawable.toast_bg_layout)
    textView.setTextColor(Color.WHITE)
    textView.textSize = 16f
    textView.text = this
    textView.setPadding(
        20f.dp2px(context), 10f.dp2px(context), 20f.dp2px(context), 10f.dp2px(context)
    )
    toast.setGravity(Gravity.BOTTOM, 0, 80f.dp2px(context))
    toast.view = textView
    toast.duration = Toast.LENGTH_SHORT
    toast.show()
}