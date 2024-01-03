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
 * 获取图片base64编码
 */
fun File.toBase64(): String {
    try {
        val fis = FileInputStream(this)
        val bos = ByteArrayOutputStream()
        val b = ByteArray(1024)
        var n: Int
        while (fis.read(b).also { n = it } != -1) {
            bos.write(b, 0, n)
        }
        fis.close()
        bos.close()
        val imgBytes = bos.toByteArray()
        val result = Base64.encodeToString(
            imgBytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
        return result.replace("-", "+").replace("_", "/")
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