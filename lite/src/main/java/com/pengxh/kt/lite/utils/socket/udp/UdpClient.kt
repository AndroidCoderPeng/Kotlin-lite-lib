package com.pengxh.kt.lite.utils.socket.udp

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.DatagramPacket
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.util.CharsetUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * UDP客户端
 *
 *  private val udpClient by lazy { UdpClient() }
 *
 *  udpClient.send("")
 *
 *  udpClient.release()
 * */
class UdpClient(private val remote: String, private val port: Int) : UdpChannelInboundHandler(),
    Runnable {

    private val bootStrap by lazy { Bootstrap() }
    private val eventLoopGroup by lazy { NioEventLoopGroup() }
    private val udpChannelInitializer by lazy { UdpChannelInitializer(this) }
    private var executorService: ExecutorService

    init {
        bootStrap.group(eventLoopGroup)
        bootStrap.channel(NioDatagramChannel::class.java)
            .option(ChannelOption.SO_RCVBUF, 1024)
            .option(ChannelOption.SO_SNDBUF, 1024)
        bootStrap.handler(udpChannelInitializer)

        executorService = Executors.newSingleThreadExecutor()
        executorService.execute(this)
    }

    override fun run() {
        try {
            val channelFuture = bootStrap.bind(port).sync()
            channelFuture.channel().closeFuture().sync()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {
            eventLoopGroup.shutdownGracefully()
        }
    }

    fun send(value: String) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                val datagramPacket = DatagramPacket(
                    Unpooled.copiedBuffer(value, CharsetUtil.UTF_8), InetSocketAddress(remote, port)
                )
                sendDatagramPacket(datagramPacket)
            }
        }
    }

    fun send(value: ByteArray) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                val datagramPacket = DatagramPacket(
                    Unpooled.copiedBuffer(value), InetSocketAddress(remote, port)
                )
                sendDatagramPacket(datagramPacket)
            }
        }
    }

    fun send(value: ByteBuf) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                val datagramPacket = DatagramPacket(
                    Unpooled.copiedBuffer(value), InetSocketAddress(remote, port)
                )
                sendDatagramPacket(datagramPacket)
            }
        }
    }

    fun release() {
        releasePort()
    }

    override fun receivedMessage(data: String) {

    }
}