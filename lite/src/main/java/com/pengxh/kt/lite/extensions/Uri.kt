package com.pengxh.kt.lite.extensions

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.FileUtils
import android.provider.MediaStore
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream

//将content路径转为path
fun Uri.realFilePath(context: Context): String {
    var path = ""
    when (this.scheme) {
        ContentResolver.SCHEME_FILE -> {
            this.path?.let {
                path = File(it).absolutePath
            }
        }

        ContentResolver.SCHEME_CONTENT -> {
            context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val displayName = cursor.getString(columnIndex)
                    context.contentResolver.openInputStream(this)?.use { inputStream ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val uniqueFileName = "${System.currentTimeMillis()}_$displayName"
                            val cache = File(context.cacheDir, uniqueFileName)
                            FileOutputStream(cache).use { fos ->
                                FileUtils.copy(inputStream, fos)
                                path = cache.absolutePath
                            }
                        } else {
                            // 处理 Android 10 以下的情况
                            val projection = arrayOf(MediaStore.Images.Media.DATA)
                            context.contentResolver.query(
                                this, projection, null, null, null
                            )?.use { cursor ->
                                if (cursor.moveToFirst()) {
                                    val index = cursor.getColumnIndexOrThrow(
                                        MediaStore.Images.Media.DATA
                                    )
                                    path = cursor.getString(index)
                                }
                            }
                        }
                    }
                }
            } ?: throw IllegalStateException("Cursor is null")
        }

        else -> throw IllegalArgumentException("Unsupported scheme: ${this.scheme}")
    }
    return path
}