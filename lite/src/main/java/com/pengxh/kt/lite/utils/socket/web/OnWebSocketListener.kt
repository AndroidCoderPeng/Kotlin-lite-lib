package com.pengxh.kt.lite.utils.socket.web

import okhttp3.Response
import okhttp3.WebSocket
import okio.ByteString

interface OnWebSocketListener {
    fun onOpen(webSocket: WebSocket, response: Response)

    fun onMessageResponse(webSocket: WebSocket, message: String)

    fun onMessageResponse(webSocket: WebSocket, bytes: ByteString)

    fun onServerDisconnected(webSocket: WebSocket, code: Int, reason: String)

    fun onClientDisconnected(webSocket: WebSocket, code: Int, reason: String)

    fun onFailure(webSocket: WebSocket)
}