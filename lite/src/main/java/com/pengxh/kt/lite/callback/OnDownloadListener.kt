package com.pengxh.kt.lite.callback

import java.io.File

interface OnDownloadListener {
    fun onDownloadStart(totalBytes: Long)

    fun onProgressChanged(currentBytes: Long)

    fun onDownloadEnd(file: File?)
}