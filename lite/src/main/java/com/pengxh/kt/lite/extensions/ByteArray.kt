package com.pengxh.kt.lite.extensions

/**
 * ByteArray转ASCII码
 * */
fun ByteArray.toAsciiCode(): String {
    //判断是否能被2整除
    return if (this.size % 2 == 0) {
        val builder = StringBuilder()
        this.forEach {
            builder.append(Char(it.toInt()))
        }
        builder.toString()
    } else {
        "Decode Error"
    }
}