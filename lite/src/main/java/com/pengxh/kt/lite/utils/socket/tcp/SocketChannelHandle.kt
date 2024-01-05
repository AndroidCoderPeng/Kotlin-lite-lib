package com.pengxh.kt.lite.utils.socket.tcp

import android.util.Log
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class SocketChannelHandle(private val listener: ISocketListener) :
    SimpleChannelInboundHandler<ByteArray>() {

    private val kTag = "SocketChannelHandle"

    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        Log.d(kTag, "channelActive ===> 连接成功")
        listener.onConnectStatusChanged(ConnectState.SUCCESS)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        super.channelInactive(ctx)
        Log.e(kTag, "channelInactive: 连接断开")
        listener.onConnectStatusChanged(ConnectState.CLOSED)
    }

    override fun channelRead0(ctx: ChannelHandlerContext, data: ByteArray?) {
        listener.onMessageResponse(data)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        super.exceptionCaught(ctx, cause)
        Log.d(kTag, "exceptionCaught ===> $cause")
        listener.onConnectStatusChanged(ConnectState.ERROR)
        ctx.close()
    }
}