package com.pengxh.kt.lite.extensions

import android.util.Base64
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.io.IOException

fun File.read(): String {
    val builder: StringBuilder
    try {
        val bufferedReader = BufferedReader(FileReader(this))
        var line = bufferedReader.readLine()
        builder = StringBuilder()
        while (line != null) {
            builder.append(line)
            builder.append("\n")
            line = bufferedReader.readLine()
        }
        bufferedReader.close()
        return builder.toString()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return ""
}

/**
 * 获取图片文件base64编码
 *
 * 如果是上传到服务器，编码格式为：Base64.NO_WRAP
 *
 * 如果是本地使用，编码格式为：Base64.DEFAULT
 *
 * 默认：Base64.NO_WRAP
 */
fun File.toBase64(): String {
    try {
        val fileInputStream = FileInputStream(this)
        val byteArrayOutputStream = ByteArrayOutputStream()
        val b = ByteArray(1024)
        var n: Int
        while (fileInputStream.read(b).also { n = it } != -1) {
            byteArrayOutputStream.write(b, 0, n)
        }
        val imgBytes = byteArrayOutputStream.toByteArray()
        fileInputStream.close()
        byteArrayOutputStream.close()
        return Base64.encodeToString(imgBytes, Base64.NO_WRAP)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/**
 * 递归计算文件夹大小
 */
fun File.calculateSize(): Long {
    var size = 0L
    val files = this.listFiles()
    if (files != null) {
        for (f in files) {
            if (f.isDirectory) {
                f.calculateSize()
            } else {
                size += f.length()
            }
        }
    }
    return size
}

/**
 * 递归删除文件
 */
fun File.deleteFile() {
    if (this.isDirectory) {
        val files = this.listFiles()
        if (files != null) {
            for (f in files) {
                f.deleteFile()
            }
        }
    } else if (this.exists()) {
        this.delete()
    }
}