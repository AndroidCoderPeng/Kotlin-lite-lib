package com.pengxh.kt.lite.utils.socket.udp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.DatagramChannel
import io.netty.channel.socket.DatagramPacket
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.handler.timeout.IdleStateHandler
import io.netty.util.CharsetUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetSocketAddress

class UdpClient(private val messageCallback: OnUdpMessageCallback) : LifecycleOwner {

    private val bootStrap by lazy { Bootstrap() }
    private val eventLoopGroup by lazy { NioEventLoopGroup() }
    private lateinit var socketAddress: InetSocketAddress
    private var channel: Channel? = null

    init {
        bootStrap.group(eventLoopGroup)
        bootStrap.channel(NioDatagramChannel::class.java)
            .option(ChannelOption.SO_RCVBUF, 1024)
            .option(ChannelOption.SO_SNDBUF, 1024)
            .handler(object : ChannelInitializer<DatagramChannel>() {
                override fun initChannel(datagramChannel: DatagramChannel) {
                    val channelPipeline = datagramChannel.pipeline()
                    channelPipeline.addLast(
                        IdleStateHandler(15, 15, 0)
                    ).addLast(UdpChannelInboundHandler(messageCallback))
                }
            })
    }

    fun bind(remote: String, port: Int) {
        this.socketAddress = InetSocketAddress(remote, port)
        lifecycleScope.launch(Dispatchers.IO) {
            val channelFuture = bootStrap.bind(port).sync()
            channel = channelFuture.channel()
            channel?.apply {
                closeFuture().sync()
            }
        }
    }

    fun sendMessage(value: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val byteBuf = Unpooled.copiedBuffer(value, CharsetUtil.UTF_8)
            val datagramPacket = DatagramPacket(byteBuf, socketAddress)
            channel?.writeAndFlush(datagramPacket)
        }
    }

    fun sendMessage(value: ByteArray) {
        lifecycleScope.launch(Dispatchers.IO) {
            val datagramPacket = DatagramPacket(Unpooled.copiedBuffer(value), socketAddress)
            channel?.writeAndFlush(datagramPacket)
        }
    }

    fun sendMessage(value: ByteBuf) {
        lifecycleScope.launch(Dispatchers.IO) {
            val datagramPacket = DatagramPacket(Unpooled.copiedBuffer(value), socketAddress)
            channel?.writeAndFlush(datagramPacket)
        }
    }

    fun release() {
        eventLoopGroup.shutdownGracefully()
    }

    private val registry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return registry
    }
}