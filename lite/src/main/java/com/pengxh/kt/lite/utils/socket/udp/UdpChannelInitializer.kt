package com.pengxh.kt.lite.utils.socket.udp

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.DatagramChannel
import io.netty.handler.timeout.IdleStateHandler


open class UdpChannelInitializer(private val handler: UdpChannelInboundHandler) :
    ChannelInitializer<DatagramChannel>() {

    override fun initChannel(datagramChannel: DatagramChannel) {
        datagramChannel.pipeline().addLast(
            IdleStateHandler(
                15, 15, 0
            )
        ).addLast(handler)
    }
}