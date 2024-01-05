package com.pengxh.kt.lite.utils.socket.tcp

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SocketManager : LifecycleOwner, ISocketListener {

    private val kTag = "SocketManager"
    private val registry = LifecycleRegistry(this)
    private val tcpClient by lazy { SocketClient() }

    override fun getLifecycle(): Lifecycle {
        return registry
    }

    fun connectServer(hostname: String, port: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (!tcpClient.isConnected) {
                tcpClient.setReconnectNum(3)
                tcpClient.setReconnectInterval(10 * 1000)
                tcpClient.setSocketListener(this@SocketManager)
                tcpClient.connect(hostname, port)
            } else {
                tcpClient.disconnect()
            }
        }
    }

    /**
     * 处理接收消息
     * */
    override fun onMessageResponse(data: ByteArray?) {

    }

    override fun onConnectStatusChanged(state: ConnectState) {
        when (state) {
            ConnectState.SUCCESS -> Log.d(kTag, "onConnectStatusChanged => 连接成功")
            ConnectState.CLOSED -> Log.d(kTag, "onConnectStatusChanged => 连接关闭")
            else -> Log.d(kTag, "onConnectStatusChanged => 连接断开，正在重连")
        }
    }

    fun sendData(bytes: ByteArray) {
        tcpClient.sendData(bytes)
    }

    fun close() {
        tcpClient.disconnect()
    }
}