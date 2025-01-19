package com.pengxh.kt.lite.extensions

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.view.WindowManager
import com.pengxh.kt.lite.utils.LiteKitConstant
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 判断是否有网络连接
 * @return
 */
fun Context.isNetworkConnected(): Boolean {
    val manager = this.getSystemService<ConnectivityManager>()
    val network = manager?.activeNetwork ?: return false
    val networkCapabilities = manager.getNetworkCapabilities(network) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

/**
 * Context内联函数-扩展函数
 * */
inline fun <reified T> Context.getSystemService(): T? {
    return this.getSystemService(T::class.java)
}

inline fun <reified T> Context.navigatePageTo() {
    startActivity(Intent(this, T::class.java))
}

inline fun <reified T> Context.navigatePageTo(value: String) {
    val intent = Intent(this, T::class.java)
    intent.putExtra(LiteKitConstant.INTENT_PARAM_KEY, value)
    startActivity(intent)
}

inline fun <reified T> Context.navigatePageTo(values: ArrayList<String>) {
    val intent = Intent(this, T::class.java)
    intent.putStringArrayListExtra(LiteKitConstant.INTENT_PARAM_KEY, values)
    startActivity(intent)
}

/**
 * @param index     展示图片的角标，从0开始
 * @param imageList
 */
inline fun <reified T> Context.navigatePageTo(index: Int, imageList: ArrayList<String>) {
    val intent = Intent(this, T::class.java)
    intent.putExtra(LiteKitConstant.BIG_IMAGE_INTENT_INDEX_KEY, index)
    intent.putStringArrayListExtra(LiteKitConstant.BIG_IMAGE_INTENT_DATA_KEY, imageList)
    startActivity(intent)
}

/**
 * 获取本地Asserts文件内容
 */
fun Context.readAssetsFile(fileName: String?): String {
    if (fileName.isNullOrBlank()) return ""
    try {
        val inputStreamReader = InputStreamReader(this.assets.open(fileName))
        val bufferedReader = BufferedReader(inputStreamReader)
        val data = StringBuilder()
        var line: String
        while (true) {
            line = bufferedReader.readLine() ?: break
            data.append(line)
        }
        return data.toString()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return ""
}

/**
 * 获取屏幕宽度
 *
 * @return
 */
fun Context.getScreenWidth(): Int {
    return this.resources.displayMetrics.widthPixels
}

/**
 * 获取屏幕高度，兼容Android 11+
 */
fun Context.getScreenHeight(): Int {
    return this.resources.displayMetrics.heightPixels + getStatusBarHeight()
}

/**
 * 获取状态栏高度，兼容Android 11+
 * */
fun Context.getStatusBarHeight(): Int {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val windowMetrics = windowManager.currentWindowMetrics
        val windowInsets = windowMetrics.windowInsets

        val insets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.statusBars())
        return insets.top
    } else {
        if (Build.MANUFACTURER.lowercase(Locale.getDefault()) == "xiaomi") {
            val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
            return if (resourceId > 0) {
                this.resources.getDimensionPixelSize(resourceId)
            } else {
                0
            }
        } else {
            try {
                val clazz = Class.forName("com.android.internal.R\$dimen")
                val obj = clazz.newInstance()
                val field = clazz.getField("status_bar_height")
                if (field[obj] == null) {
                    return 0
                }
                val x = field[obj]!!.toString().toInt()
                if (x > 0) {
                    return this.resources.getDimensionPixelSize(x)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return 0
        }
    }
}

/**
 * 获取屏幕密度比值
 */
fun Context.getScreenDensity(): Float {
    val windowManager = this.getSystemService<WindowManager>()!!
    val displayMetrics = DisplayMetrics()
    val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this.display
    } else {
        windowManager.defaultDisplay
    }
    if (display == null) {
        return 1f
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this.resources.displayMetrics.density
    } else {
        display.getMetrics(displayMetrics)
        displayMetrics.density
    }
}


fun Context.createLogFile(): File {
    val documentDir = File(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "")
    val timeStamp = SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(Date())
    val logFile = File(documentDir.toString() + File.separator + "Log_" + timeStamp + ".txt")
    if (!logFile.exists()) {
        try {
            logFile.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return logFile
}

/**
 * 音频编解码：AMR_WB
 *
 * 音频文件格式：.amr
 * */
fun Context.createAudioFile(): File {
    val audioDir = File(this.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "")
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date())
    val audioFile = File(audioDir.toString() + File.separator + "AUD_" + timeStamp + ".amr")
    if (!audioFile.exists()) {
        try {
            audioFile.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return audioFile
}

fun Context.createVideoFileDir(): File {
    val videoDir = File(this.getExternalFilesDir(Environment.DIRECTORY_MOVIES), "")
    if (!videoDir.exists()) {
        videoDir.mkdir()
    }
    return videoDir
}


fun Context.createDownloadFileDir(): File {
    val downloadDir = File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "")
    if (!downloadDir.exists()) {
        downloadDir.mkdir()
    }
    return downloadDir
}

fun Context.createImageFileDir(): File {
    val imageDir = File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "")
    if (!imageDir.exists()) {
        imageDir.mkdir()
    }
    return imageDir
}

fun Context.createCompressImageDir(): File {
    val imageDir = File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "CompressImage")
    if (!imageDir.exists()) {
        imageDir.mkdir()
    }
    return imageDir
}