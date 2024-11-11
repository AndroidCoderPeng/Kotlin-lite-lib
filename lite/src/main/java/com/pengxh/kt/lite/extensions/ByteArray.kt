package com.pengxh.kt.lite.extensions

/**
 * ByteArray转ASCII码
 * */
fun ByteArray.toAsciiCode(): String {
    val builder = StringBuilder()
    this.forEach {
        builder.append(Char(it.toInt()))
    }
    return builder.toString()
}