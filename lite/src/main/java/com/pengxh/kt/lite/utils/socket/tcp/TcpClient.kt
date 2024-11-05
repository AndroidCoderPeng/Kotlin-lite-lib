package com.pengxh.kt.lite.utils.socket.tcp

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import io.netty.bootstrap.Bootstrap
import io.netty.channel.AdaptiveRecvByteBufAllocator
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.bytes.ByteArrayDecoder
import io.netty.handler.codec.bytes.ByteArrayEncoder
import io.netty.handler.timeout.IdleStateHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

class TcpClient(
    private val host: String,
    private val port: Int,
    private val listener: OnTcpConnectStateListener
) : LifecycleOwner {

    private val registry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return registry
    }

    private val kTag = "TcpClient"
    private val reconnectDelay = 5L
    private var bootstrap: Bootstrap = Bootstrap()
    private var loopGroup: EventLoopGroup = NioEventLoopGroup()
    private var channel: Channel? = null
    private var isRunning = false
    private var retryTimes = 0

    fun start() {
        if (isRunning) {
            return
        }
        bootstrap.group(loopGroup)
            .channel(NioSocketChannel::class.java)
            .option(ChannelOption.TCP_NODELAY, true) //无阻塞
            .option(ChannelOption.SO_KEEPALIVE, true) //长连接
            .option(
                ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator(5000, 5000, 8000)
            )
            .handler(SimpleChannelInitializer())
        connect()
    }

    private inner class SimpleChannelInitializer : ChannelInitializer<SocketChannel>() {
        override fun initChannel(ch: SocketChannel?) {
            ch?.apply {
                pipeline()
                    .addLast(ByteArrayDecoder())
                    .addLast(ByteArrayEncoder())
                    .addLast(IdleStateHandler(60, 10, 0))
                    .addLast(object : SimpleChannelInboundHandler<ByteArray>() {
                        override fun channelActive(ctx: ChannelHandlerContext) {
                            val address = ctx.channel().remoteAddress() as InetSocketAddress
                            Log.d(kTag, "${address.address.hostAddress} 已连接")
                            listener.onConnected()
                        }

                        override fun channelInactive(ctx: ChannelHandlerContext) {
                            val address = ctx.channel().remoteAddress() as InetSocketAddress
                            Log.d(kTag, "${address.address.hostAddress} 已断开")
                            listener.onDisconnected()
                            reconnect()
                        }

                        override fun channelRead0(
                            ctx: ChannelHandlerContext, msg: ByteArray?
                        ) {
                            listener.onMessageReceived(msg)
                        }

                        override fun exceptionCaught(
                            ctx: ChannelHandlerContext, cause: Throwable
                        ) {
                            Log.d(kTag, "exceptionCaught: ${cause.message}")
                            listener.onConnectFailed()
                            ctx.close()
                        }
                    })
            }
        }
    }

    private fun connect() {
        if (channel != null && channel!!.isActive) {
            return
        }
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val channelFuture = bootstrap.connect(host, port)
                    .addListener(object : ChannelFutureListener {
                        override fun operationComplete(channelFuture: ChannelFuture) {
                            if (channelFuture.isSuccess) {
                                isRunning = true
                                retryTimes = 0
                                channel = channelFuture.channel()
                            }
                        }
                    }).sync()
                channelFuture.channel().closeFuture().sync()
            } catch (e: Exception) {
                Log.d(kTag, "连接失败: ${e.message}")
                reconnect()
            }
        }
    }

    private fun reconnect() {
        retryTimes++
        Log.d(kTag, "开始第 $retryTimes 次重连")
        loopGroup.schedule({ connect() }, reconnectDelay, TimeUnit.SECONDS)
    }

    fun stop() {
        isRunning = false
        channel?.close()
        loopGroup.shutdownGracefully()
    }

    fun sendMessage(bytes: ByteArray) {
        if (!isRunning) {
            return
        }
        channel?.writeAndFlush(bytes)
    }
}