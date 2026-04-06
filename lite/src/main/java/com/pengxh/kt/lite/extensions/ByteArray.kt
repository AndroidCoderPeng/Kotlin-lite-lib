package com.pengxh.kt.lite.extensions

/**
 * ByteArray转Hex
 * */
fun ByteArray.toHex(): String {
    val hexArray = "0123456789ABCDEF".toCharArray()
    val hexChars = CharArray(this.size * 2)
    for (j in this.indices) {
        val i = this[j].toInt() and 0xFF
        hexChars[j * 2] = hexArray[i ushr 4]
        hexChars[j * 2 + 1] = hexArray[i and 0x0F]
    }
    return String(hexChars)
}

/**
 * ByteArray转ASCII
 * */
fun ByteArray.toAscIICode(): String {
    val builder = StringBuilder()
    forEach {
        builder.append(Char(it.toInt()))
    }
    return builder.toString()
}