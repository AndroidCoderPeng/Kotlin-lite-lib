package com.pengxh.kt.lite.utils.socket.tcp

interface OnTcpConnectStateListener {
    fun onConnected()
    fun onDisconnected()
    fun onConnectFailed()
    fun onMessageReceived(bytes: ByteArray?)
}