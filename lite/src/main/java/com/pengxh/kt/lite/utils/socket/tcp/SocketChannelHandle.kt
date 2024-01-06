package com.pengxh.kt.lite.utils.socket.tcp

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class SocketChannelHandle(private val listener: OnSocketListener) :
    SimpleChannelInboundHandler<ByteArray>() {

    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        listener.onConnectStateChanged(ConnectState.SUCCESS)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        super.channelInactive(ctx)
        listener.onConnectStateChanged(ConnectState.CLOSED)
    }

    override fun channelRead0(ctx: ChannelHandlerContext, data: ByteArray?) {
        listener.onMessageResponse(data)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        super.exceptionCaught(ctx, cause)
        listener.onConnectStateChanged(ConnectState.ERROR)
        ctx.close()
    }
}