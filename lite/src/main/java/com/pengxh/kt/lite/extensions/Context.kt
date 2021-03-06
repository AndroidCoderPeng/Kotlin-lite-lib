package com.pengxh.kt.lite.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import com.pengxh.kt.lite.utils.Constant
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

/**
 * 判断是否有网络连接
 * @return
 */
@SuppressLint("MissingPermission")
fun Context.isNetworkConnected(): Boolean { //true是连接，false是没连接
    val manager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (manager == null) {
        return false
    } else {
        val netWorkInfo = manager.activeNetworkInfo
        if (netWorkInfo != null) {
            return netWorkInfo.isAvailable
        }
    }
    return false
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
    intent.putExtra(Constant.INTENT_PARAM, value)
    startActivity(intent)
}

inline fun <reified T> Context.navigatePageTo(values: ArrayList<String>) {
    val intent = Intent(this, T::class.java)
    intent.putStringArrayListExtra(Constant.INTENT_PARAM, values)
    startActivity(intent)
}

/**
 * @param index     展示图片的角标，从0开始
 * @param imageList
 */
inline fun <reified T> Context.navigatePageTo(index: Int, imageList: ArrayList<String>) {
    val intent = Intent(this, T::class.java)
    intent.putExtra(Constant.BIG_IMAGE_INTENT_INDEX_KEY, index)
    intent.putStringArrayListExtra(Constant.BIG_IMAGE_INTENT_DATA_KEY, imageList)
    startActivity(intent)
}

/**
 * 获取本地Asserts文件内容
 */
fun Context.readAssetsFile(fileName: String?): String {
    if (fileName.toString().isBlank()) return ""
    try {
        val inputStreamReader = InputStreamReader(this.assets.open(fileName!!))
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

//获取SimSerialNumber
@SuppressLint("MissingPermission", "HardwareIds")
fun Context.obtainSimCardSerialNumber(): String? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        //Android 10改为获取Android_ID
        return Settings.System.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
    } else {
        val telephony = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val telephonyClass: Class<*>
        try {
            telephonyClass = Class.forName(telephony.javaClass.name)
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return ""
            }
            val imei = telephony.deviceId
            return if (TextUtils.isEmpty(imei)) {
                val m = telephonyClass.getMethod(
                    "getSimSerialNumber", Int::class.javaPrimitiveType
                )
                //主卡，卡1
                val mainCard = m.invoke(telephony, 0) as String
                //副卡，卡2
                val otherCard = m.invoke(telephony, 1) as String
                if (TextUtils.isEmpty(mainCard)) {
                    otherCard
                } else {
                    mainCard
                }
            } else {
                imei
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return ""
}

/**
 * 获取屏幕宽度
 *
 * @return
 */
fun Context.obtainScreenWidth(): Int {
    return this.resources.displayMetrics.widthPixels
}

/**
 * 获取屏幕高度
 *
 * @return
 */
fun Context.obtainScreenHeight(): Int {
    var height = 0
    val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        height = this.resources.getDimensionPixelSize(resourceId)
    }
    return this.resources.displayMetrics.heightPixels + height
}

/**
 * 获取屏幕密度
 *
 * Dpi（dots per inch 像素密度）
 * Density 密度
 */
fun Context.obtainScreenDensity(): Float {
    val manager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val dm = DisplayMetrics()
    manager.defaultDisplay.getMetrics(dm)
    return dm.density
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

fun Context.createAudioFile(): File {
    val audioDir = File(this.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "")
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date())
    val audioFile = File(audioDir.toString() + File.separator + "AUD_" + timeStamp + ".m4a")
    if (!audioFile.exists()) {
        try {
            audioFile.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return audioFile
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