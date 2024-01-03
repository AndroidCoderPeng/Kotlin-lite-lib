package com.pengxh.kt.lite.extensions

/**
 * ByteArray转Hex
 * */
fun ByteArray.toHex(): String {
    val hexArray = "0123456789ABCDEF".toCharArray()
    val hexChars = CharArray(this.size * 2)
    for (j in this.indices) {
        val v: Int = this[j].toInt() and 0xFF
        hexChars[j * 2] = hexArray[v ushr 4]
        hexChars[j * 2 + 1] = hexArray[v and 0x0F]
    }
    return String(hexChars)
}