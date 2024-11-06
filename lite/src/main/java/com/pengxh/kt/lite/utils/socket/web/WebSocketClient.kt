package com.pengxh.kt.lite.utils.socket.web

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.util.concurrent.TimeUnit

class WebSocketClient(private val listener: OnWebSocketListener) : LifecycleOwner {
    private val kTag = "WebSocketClient"
    private val httpClient = OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build()
    private val reconnectDelay: Long = 5000
    private lateinit var url: String
    private lateinit var webSocket: WebSocket
    private var isRunning = false
    private var retryTimes = 0

    private val registry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return registry
    }

    /**
     * WebSocketClient 是否正在运行
     * */
    fun isRunning(): Boolean {
        return isRunning
    }

    fun start(url: String) {
        this.url = url
        if (isRunning) {
            return
        }
        connect()
    }

    @Synchronized
    private fun connect() {
        Log.d(kTag, "connect: $url")
        val request = Request.Builder().url(url).build()
        webSocket = httpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                listener.onOpen(webSocket, response)
                isRunning = true
                retryTimes = 0
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
    }

    private fun reconnect() {
        isRunning = false
        lifecycleScope.launch(Dispatchers.IO) {
            retryTimes++
            Log.d(kTag, "开始第 $retryTimes 次重连")
            connect()
            delay(reconnectDelay)
        }
    }

    fun stop() {
        Log.d(kTag, "$url 断开连接")
        webSocket.close(1000, "Application Request Close")
        isRunning = false
    }
}