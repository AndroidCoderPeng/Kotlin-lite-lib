package com.pengxh.kt.lite.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FileDownloadManager : LifecycleOwner {
    private val registry = LifecycleRegistry(this)
    private val httpClient by lazy { OkHttpClient() }
    private lateinit var url: String
    private lateinit var suffix: String
    private lateinit var directory: File
    private lateinit var downloadListener: OnFileDownloadListener

    /**
     * 文件下载地址
     * */
    fun setDownloadFileSource(url: String): FileDownloadManager {
        this.url = url
        return this
    }

    /**
     * 文件后缀
     *  如：apk等
     * */
    fun setFileSuffix(suffix: String): FileDownloadManager {
        this.suffix = if (suffix.contains(".")) {
            //去掉前缀的点
            suffix.drop(1)
        } else {
            suffix
        }
        return this
    }

    /**
     * 文件保存的地址
     * */
    fun setFileSaveDirectory(directory: File): FileDownloadManager {
        this.directory = directory
        return this
    }

    /**
     * 设置文件下载回调监听
     * */
    fun setOnFileDownloadListener(downloadListener: OnFileDownloadListener): FileDownloadManager {
        this.downloadListener = downloadListener
        return this
    }

    /**
     * 开始下载
     * */
    fun start() {
        if (url.isBlank()) {
            downloadListener.onFailure(IllegalArgumentException("url is empty"))
            return
        }

        val request = Request.Builder().get().url(url).build()
        val newCall = httpClient.newCall(request)
        /**
         * 如果已被加入下载队列，则取消之前的，重新下载
         */
        if (newCall.isExecuted()) {
            newCall.cancel()
        }

        //异步下载文件
        newCall.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                lifecycleScope.launch(Dispatchers.Main) {
                    downloadListener.onFailure(e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.apply {
                    val inputStream = this.byteStream()
                    val fileSize = this.contentLength()

                    val file = File(directory, "${System.currentTimeMillis()}.${suffix}")
                    val fileOutputStream = FileOutputStream(file)
                    val buffer = ByteArray(2048)
                    var read: Int
                    var sum = 0L
                    while (inputStream.read(buffer).also { read = it } != -1) {
                        fileOutputStream.write(buffer, 0, read)
                        sum += read.toLong()
                        lifecycleScope.launch(Dispatchers.Main) {
                            val progress = (sum * 1.0 / fileSize * 100).toInt()
                            downloadListener.onProgressChanged(progress)
                        }
                    }
                    lifecycleScope.launch(Dispatchers.Main) {
                        downloadListener.onDownloadEnd(file)
                    }
                    fileOutputStream.flush()
                    //关闭流
                    fileOutputStream.close()
                    inputStream.close()
                }
            }
        })
    }

    interface OnFileDownloadListener {
        fun onProgressChanged(progress: Int)

        fun onDownloadEnd(file: File)

        fun onFailure(throwable: Throwable)
    }

    override fun getLifecycle(): Lifecycle {
        return registry
    }
}