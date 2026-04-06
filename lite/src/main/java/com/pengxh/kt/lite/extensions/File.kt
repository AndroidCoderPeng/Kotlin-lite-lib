package com.pengxh.kt.lite.extensions

import java.io.File
import java.util.Base64

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
    val imageBytes = readBytes()
    return Base64.getEncoder().encodeToString(imageBytes)
}

/**
 * 递归计算文件夹大小
 */
fun File.calculateSize(): Long {
    var size = 0L
    val files = listFiles()
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
    if (isDirectory) {
        val files = listFiles()
        if (files != null) {
            for (f in files) {
                f.deleteFile()
            }
        }
    } else if (exists()) {
        delete()
    }
}