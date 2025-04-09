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
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class TcpClient(private val listener: OnTcpConnectStateListener) {

    private val kTag = "TcpClient"
    private val reconnectDelay = 15L
    private val maxRetryTimes = 10 // 设置最大重连次数
    private val loopGroup by lazy { NioEventLoopGroup() }
    private var needReconnect = false
    private lateinit var host: String
    private var port: Int = 0
    private var channel: Channel? = null
    private var scope: CoroutineScope? = null

    @Volatile
    private var isRunning = AtomicBoolean(false)

    @Volatile
    private var retryTimes = AtomicInteger(0)

    /**
     * TcpClient 是否正在运行
     * */
    fun isRunning(): Boolean {
        return isRunning.get()
    }

    fun start(host: String, port: Int) {
        this.host = host
        this.port = port
        if (isRunning.get()) {
            Log.d(kTag, "start: TcpClient 正在运行")
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
                .addLast(IdleStateHandler(15, 15, 60, TimeUnit.SECONDS))//如果连接没有接收或发送数据超过60秒钟就发送一次心跳
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
                        if (needReconnect) {
                            reconnect()
                        }
                    }

                    override fun channelRead0(ctx: ChannelHandlerContext, msg: ByteArray?) {
                        listener.onMessageReceived(msg)
                    }

                    @Deprecated("Deprecated in Java")
                    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
                        Log.d(kTag, "exceptionCaught: ${cause.message}")
                        listener.onConnectFailed()
                        ctx.close()
                        isRunning.set(false)
                    }
                })
        }
    }

    @Synchronized
    private fun connect() {
        if (channel != null && channel!!.isActive) {
            Log.d(kTag, "connect: TcpClient 正在运行")
            return
        }
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        scope?.launch(Dispatchers.IO) {
            try {
                Log.d(kTag, "start connect: ${host}:${port}")
                var bootStrap = Bootstrap().apply {
                    group(loopGroup)
                    channel(NioSocketChannel::class.java)
                    option(ChannelOption.TCP_NODELAY, true) //无阻塞
                    option(ChannelOption.SO_KEEPALIVE, true) //长连接
                    option(
                        ChannelOption.RCVBUF_ALLOCATOR,
                        AdaptiveRecvByteBufAllocator(5000, 5000, 8000)
                    )
                    option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    handler(SimpleChannelInitializer())
                }
                val channelFuture = bootStrap.connect(host, port)
                    .addListener(object : ChannelFutureListener {
                        override fun operationComplete(channelFuture: ChannelFuture) {
                            if (channelFuture.isSuccess) {
                                isRunning.set(true)
                                retryTimes.set(0)
                                channel = channelFuture.channel()
                            }
                        }
                    }).sync()
                channelFuture.channel().closeFuture().sync()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
                reconnect()
            }
        }
    }

    private fun reconnect() {
        val currentRetryTimes = retryTimes.incrementAndGet()
        if (currentRetryTimes <= maxRetryTimes) {
            Log.d(kTag, "reconnect: 开始第 $currentRetryTimes 次重连")
            loopGroup.schedule({ connect() }, reconnectDelay, TimeUnit.SECONDS)
        } else {
            Log.e(kTag, "达到最大重连次数，停止重连")
            listener.onConnectFailed()
        }
    }

    fun stop(needReconnect: Boolean) {
        this.needReconnect = needReconnect
        isRunning.set(false)
        channel?.close()
        scope?.cancel()
    }

    fun sendMessage(bytes: ByteArray) {
        if (!isRunning.get()) {
            return
        }
        channel?.writeAndFlush(bytes)
    }
}