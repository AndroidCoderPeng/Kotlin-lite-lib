package com.pengxh.kt.lite.utils.socket.udp

interface OnUdpMessageCallback {
    /**
     * 当接收到系统消息
     */
    fun onReceivedUdpMessage(data: ByteArray)
}