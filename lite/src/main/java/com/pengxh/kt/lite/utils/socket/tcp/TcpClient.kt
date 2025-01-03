package com.pengxh.kt.lite.utils.socket.tcp

import android.util.Log
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

class TcpClient(private val listener: OnTcpConnectStateListener) {

    private val kTag = "TcpClient"
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val reconnectDelay = 5L
    private var bootStrap: Bootstrap = Bootstrap()
    private var loopGroup: EventLoopGroup = NioEventLoopGroup()
    private lateinit var host: String
    private var port: Int = 0
    private var channel: Channel? = null
    private var isRunning = false
    private var retryTimes = 0

    init {
        bootStrap.group(loopGroup)
            .channel(NioSocketChannel::class.java)
            .option(ChannelOption.TCP_NODELAY, true) //无阻塞
            .option(ChannelOption.SO_KEEPALIVE, true) //长连接
            .option(
                ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator(5000, 5000, 8000)
            )
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .handler(SimpleChannelInitializer())
    }

    /**
     * TcpClient 是否正在运行
     * */
    fun isRunning(): Boolean {
        return isRunning
    }

    fun start(host: String, port: Int) {
        this.host = host
        this.port = port
        if (isRunning) {
            return
        }
        connect()
    }

    private inner class SimpleChannelInitializer : ChannelInitializer<SocketChannel>() {
        override fun initChannel(ch: SocketChannel) {
            val channelPipeline = ch.pipeline()
            channelPipeline
                .addLast(ByteArrayDecoder())
                .addLast(ByteArrayEncoder())
                .addLast(IdleStateHandler(0, 0, 60, TimeUnit.SECONDS))//如果连接没有接收或发送数据超过60秒钟就发送一次心跳
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

                    override fun channelRead0(ctx: ChannelHandlerContext, msg: ByteArray?) {
                        listener.onMessageReceived(msg)
                    }

                    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
                        Log.d(kTag, "exceptionCaught: ${cause.message}")
                        listener.onConnectFailed()
                        ctx.close()
                    }
                })
        }
    }

    @Synchronized
    private fun connect() {
        if (channel != null && channel!!.isActive) {
            return
        }
        scope.launch(Dispatchers.IO) {
            try {
                val channelFuture = bootStrap.connect(host, port)
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
        scope.cancel()
    }

    fun sendMessage(bytes: ByteArray) {
        if (!isRunning) {
            return
        }
        channel?.writeAndFlush(bytes)
    }
}