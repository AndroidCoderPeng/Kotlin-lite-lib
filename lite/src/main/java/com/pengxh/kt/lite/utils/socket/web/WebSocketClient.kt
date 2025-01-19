package com.pengxh.kt.lite.utils.socket.web

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock

class WebSocketClient(private val listener: OnWebSocketListener) {

    private val kTag = "WebSocketClient"
    private val httpClient = OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build()
    private val reconnectDelay: Long = 5000
    private var maxRetryTimes = 10 // 设置最大重连次数
    private lateinit var url: String
    private lateinit var webSocket: WebSocket

    @Volatile
    private var isRunning = AtomicBoolean(false)

    @Volatile
    private var retryTimes = AtomicInteger(0)
    private val lock = ReentrantLock()

    /**
     * WebSocketClient 是否正在运行
     * */
    fun isRunning(): Boolean {
        return isRunning.get()
    }

    fun start(url: String) {
        if (url.isEmpty() || !url.startsWith("ws://") && !url.startsWith("wss://")) {
            Log.e(kTag, "Invalid URL: $url")
            listener.onFailure(null)
            return
        }
        this.url = url
        if (isRunning.get()) {
            return
        }
        connect()
    }

    private fun connect() {
        lock.lock()
        try {
            Log.d(kTag, "connect: $url")
            val request = Request.Builder().url(url).build()
            webSocket = httpClient.newWebSocket(request, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                    listener.onOpen(webSocket, response)
                    isRunning.set(true)
                    retryTimes.set(0)
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    super.onMessage(webSocket, text)
                    listener.onMessageResponse(webSocket, text)
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    super.onMessage(webSocket, bytes)
                    listener.onMessageResponse(webSocket, bytes)
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosing(webSocket, code, reason)
                    listener.onServerDisconnected(webSocket, code, reason)
                    /**
                     * APP主动断开，code = 1000
                     * 服务器主动断开，code = 1001
                     *
                     *
                     * APP主动断开，onClosing和onClosed都会走
                     * 服务器主动断开，只走onClosing
                     */
                    if (code == 1001) {
                        Log.d(kTag, "$code, $reason")
                        reconnect()
                    }
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosed(webSocket, code, reason)
                    Log.d(kTag, "$code, $reason")
                    listener.onClientDisconnected(webSocket, code, reason)
                    reconnect()
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    super.onFailure(webSocket, t, response)
                    listener.onFailure(webSocket)
                    reconnect()
                }
            })
        } finally {
            lock.unlock()
        }
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    private fun reconnect() {
        scope.launch {
            if (retryTimes.get() <= maxRetryTimes) {
                val currentRetryTimes = retryTimes.incrementAndGet()
                Log.d(kTag, "开始第 $currentRetryTimes 次重连")
                delay(reconnectDelay)
                withContext(Dispatchers.IO) { connect() }
            } else {
                Log.e(kTag, "达到最大重连次数，停止重连")
                listener.onMaxRetryReached()
            }
        }
    }

    fun stop() {
        Log.d(kTag, "$url 断开连接")
        webSocket.close(1000, "Application Request Close")
        isRunning.set(false)
    }
}