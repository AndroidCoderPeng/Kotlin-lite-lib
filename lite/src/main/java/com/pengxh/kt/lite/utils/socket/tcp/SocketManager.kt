package com.pengxh.kt.lite.utils.socket.tcp

import android.util.Log

class SocketManager private constructor() : ISocketListener {

    private val kTag = "SocketManager"
    private var nettyClient = SocketClient()

    companion object {
        val get: SocketManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { SocketManager() }
    }

    fun connectServer(hostname: String, port: Int) {
        Thread {
            if (!nettyClient.connectStatus) {
                nettyClient.setSocketListener(this)
                nettyClient.connect(hostname, port)
            } else {
                nettyClient.disconnect()
            }
        }.start()
    }

    /**
     * 处理接收消息
     * */
    override fun onMessageResponse(data: ByteArray?) {

    }

    override fun onConnectStatusChanged(state: ConnectState) {
        if (state == ConnectState.SUCCESS) {
            if (nettyClient.connectStatus) {
                Log.d(kTag, "连接成功")
            }
        } else {
            if (!nettyClient.connectStatus) {
                Log.e(kTag, "onConnectStatusChanged: 连接断开，正在重连")
            }
        }
    }

    fun sendData(bytes: ByteArray) {
        nettyClient.sendData(bytes)
    }

    fun close() {
        nettyClient.disconnect()
    }
}