package com.pengxh.kt.lite.utils.socket.tcp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SocketManager constructor(private val listener: OnSocketListener) : LifecycleOwner {

    private val tcpClient by lazy { SocketClient() }

    fun connectServer(hostname: String, port: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (!tcpClient.isConnected) {
                tcpClient.setRetryTimes(3)
                tcpClient.setRetryInterval(10 * 1000)
                tcpClient.setSocketListener(listener)
                tcpClient.connect(hostname, port)
            } else {
                tcpClient.disconnect()
            }
        }
    }

    fun sendData(bytes: ByteArray) {
        tcpClient.sendData(bytes)
    }

    fun close() {
        tcpClient.disconnect()
    }

    private val registry = LifecycleRegistry(this)
    override fun getLifecycle(): Lifecycle {
        return registry
    }
}