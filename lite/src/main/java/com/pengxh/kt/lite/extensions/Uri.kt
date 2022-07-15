package com.pengxh.kt.lite.extensions

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.FileUtils
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream

//将content路径转为path
fun Uri.realFilePath(context: Context): String {
    var path = ""
    if (this.scheme == ContentResolver.SCHEME_FILE) {
        path = File(this.path!!).absolutePath
    } else if (this.scheme == ContentResolver.SCHEME_CONTENT) {
        val cursor = context.contentResolver.query(this, null, null, null, null)!!
        if (cursor.moveToFirst()) {
            try {
                val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val displayName = cursor.getString(columnIndex)
                val inputStream = context.contentResolver.openInputStream(this)
                if (inputStream != null) {
                    //Android 10需要转移到沙盒
                    val cache = File(context.cacheDir.absolutePath, displayName)
                    val fos = FileOutputStream(cache)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        FileUtils.copy(inputStream, fos)
                        path = cache.absolutePath
                        fos.close()
                        inputStream.close()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor.close()
            }
        }
    }
    return path
}