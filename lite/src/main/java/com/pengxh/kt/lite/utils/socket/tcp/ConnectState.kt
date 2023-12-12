package com.pengxh.kt.lite.utils.socket.tcp

sealed class ConnectState {
    /**
     * 连接成功
     * */
    object SUCCESS : ConnectState()

    /**
     * 关闭连接
     * */
    object CLOSED : ConnectState()

    /**
     * 连接失败
     * */
    object ERROR : ConnectState()
}
