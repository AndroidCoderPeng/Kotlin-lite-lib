package com.pengxh.kt.lite.utils.socket.tcp

interface OnTcpMessageCallback {
    /**
     * 当接收到系统消息
     */
    fun onReceivedTcpMessage(data: ByteArray?)

    /**
     * 当连接状态发生变化时调用
     */
    fun onConnectStateChanged(state: ConnectState)
}