package com.pengxh.kt.lite.utils

import android.os.Handler
import android.os.Message
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

class FileDownloadManager(builder: Builder) : Handler.Callback {

    private val httpClient by lazy { OkHttpClient() }
    private val weakReferenceHandler by lazy { WeakReferenceHandler(this) }

    class Builder {
        lateinit var url: String
        lateinit var suffix: String
        lateinit var directory: File
        lateinit var downloadListener: OnFileDownloadListener

        /**
         * 文件下载地址
         * */
        fun setDownloadFileSource(url: String): Builder {
            this.url = url
            return this
        }

        /**
         * 文件后缀
         *  如：apk等
         * */
        fun setFileSuffix(suffix: String): Builder {
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
        fun setFileSaveDirectory(directory: File): Builder {
            this.directory = directory
            return this
        }

        /**
         * 设置文件下载回调监听
         * */
        fun setOnFileDownloadListener(downloadListener: OnFileDownloadListener): Builder {
            this.downloadListener = downloadListener
            return this
        }

        fun build(): FileDownloadManager {
            if (!::url.isInitialized || !::suffix.isInitialized || !::directory.isInitialized || !::downloadListener.isInitialized) {
                throw IllegalStateException("All properties must be initialized before building.")
            }
            return FileDownloadManager(this)
        }
    }

    private val url = builder.url
    private val suffix = builder.suffix
    private val directory = builder.directory
    private val listener = builder.downloadListener

    /**
     * 开始下载
     * */
    fun start() {
        val request = Request.Builder().get().url(url).build()
        val newCall = httpClient.newCall(request)
        val isExecuting = AtomicBoolean(false)

        /**
         * 如果已被加入下载队列，则取消之前的，重新下载
         */
        if (isExecuting.getAndSet(true)) {
            newCall.cancel()
        }

        //异步下载文件
        newCall.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                weakReferenceHandler.apply {
                    val message = obtainMessage()
                    message.what = downloadFailedCode
                    message.obj = e
                    sendMessage(message)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body
                if (body == null) {
                    weakReferenceHandler.apply {
                        val message = obtainMessage()
                        message.what = downloadFailedCode
                        message.obj = IOException("Response body is null")
                        sendMessage(message)
                    }
                    throw IOException("Response body is null")
                } else {
                    val inputStream = body.byteStream()
                    val fileSize = body.contentLength()
                    if (fileSize <= 0) {
                        weakReferenceHandler.apply {
                            val message = obtainMessage()
                            message.what = downloadFailedCode
                            message.obj = IllegalArgumentException("Invalid file size")
                            sendMessage(message)
                        }
                        throw IllegalArgumentException("Invalid file size")
                    }
                    weakReferenceHandler.apply {
                        val message = obtainMessage()
                        message.what = downloadStartCode
                        message.obj = fileSize
                        sendMessage(message)
                    }

                    val file = File(directory, "${System.currentTimeMillis()}.${suffix}")
                    file.outputStream().use { fos ->
                        val buffer = ByteArray(2048)
                        var sum = 0L
                        var read: Int
                        while (inputStream.read(buffer).also { read = it } != -1) {
                            fos.write(buffer, 0, read)
                            sum += read.toLong()
                            weakReferenceHandler.apply {
                                val message = obtainMessage()
                                message.what = progressChangedCode
                                message.obj = sum
                                sendMessage(message)
                            }
                        }
                    }
                    weakReferenceHandler.apply {
                        val message = obtainMessage()
                        message.what = downloadEndCode
                        message.obj = file
                        sendMessage(message)
                    }
                }
            }
        })
    }

    private val downloadStartCode = 1
    private val progressChangedCode = 2
    private val downloadEndCode = 3
    private val downloadFailedCode = 4

    override fun handleMessage(msg: Message): Boolean {
        when (msg.what) {
            downloadStartCode -> {
                listener.onDownloadStart(msg.obj as Long)
            }

            progressChangedCode -> {
                listener.onProgressChanged(msg.obj as Long)
            }

            downloadEndCode -> {
                listener.onDownloadEnd(msg.obj as File)
            }

            downloadFailedCode -> {
                listener.onDownloadFailed(msg.obj as Exception)
            }
        }
        return true
    }

    interface OnFileDownloadListener {
        fun onDownloadStart(total: Long)
        fun onProgressChanged(progress: Long)
        fun onDownloadEnd(file: File)
        fun onDownloadFailed(t: Throwable)
    }
}