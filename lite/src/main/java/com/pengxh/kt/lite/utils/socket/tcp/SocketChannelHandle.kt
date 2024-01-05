package com.pengxh.kt.lite.utils.socket.tcp

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class SocketChannelHandle(private val listener: ISocketListener) :
    SimpleChannelInboundHandler<ByteArray>() {

    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        listener.onConnectStatusChanged(ConnectState.SUCCESS)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        super.channelInactive(ctx)
        listener.onConnectStatusChanged(ConnectState.CLOSED)
    }

    override fun channelRead0(ctx: ChannelHandlerContext, data: ByteArray?) {
        listener.onMessageResponse(data)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        super.exceptionCaught(ctx, cause)
        listener.onConnectStatusChanged(ConnectState.ERROR)
        ctx.close()
    }
}