package com.pengxh.kt.lite.utils.socket.tcp

import android.os.SystemClock
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
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.bytes.ByteArrayDecoder
import io.netty.handler.codec.bytes.ByteArrayEncoder
import io.netty.handler.timeout.IdleStateHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SocketClient constructor(private val socketListener: OnSocketListener) : LifecycleOwner {

    private val kTag = "SocketClient"
    private var host = ""
    private var port = 0
    private var isConnected = false
    private lateinit var eventLoopGroup: NioEventLoopGroup
    private lateinit var channel: Channel

    /*************************************/
    //默认重连3次，每次间隔10s
    private var retryTimes = 3
    private var retryInterval = 5 * 1000L

    fun setRetryTimes(retryTimes: Int) {
        this.retryTimes = retryTimes
    }

    fun setRetryInterval(retryInterval: Long) {
        this.retryInterval = retryInterval
    }

    fun connectServer(host: String, port: Int) {
        this.host = host
        this.port = port
        if (!isConnected) {
            /**
             * 使用协程连接TCP Server
             * */
            connect()
        } else {
            eventLoopGroup.shutdownGracefully()
            isConnected = false
            retryTimes = 0
        }
    }

    /*************************************/

    private fun connect() {
        Log.d(kTag, "connect: 连接TCP服务器")
        eventLoopGroup = NioEventLoopGroup()
        val bootstrap = Bootstrap()
        bootstrap.group(eventLoopGroup)
            .channel(NioSocketChannel::class.java)
            .option(ChannelOption.TCP_NODELAY, true) //无阻塞
            .option(ChannelOption.SO_KEEPALIVE, true) //长连接
            .option(
                ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator(5000, 5000, 8000)
            )
            .handler(object : ChannelInitializer<SocketChannel>() {
                override fun initChannel(channel: SocketChannel) {
                    val pipeline = channel.pipeline()
                    /**
                     * 参数3：将在未执行读取或写入时触发超时回调，0代表不处理;
                     *
                     * 读超时尽量设置大于写超时，代表多次写超时时写心跳包，多次写了心跳数据仍然读超时代表当前连接错误，即可断开连接重新连接
                     * */
                    pipeline.addLast(IdleStateHandler(60, 10, 0))
                    pipeline.addLast(ByteArrayDecoder())
                    pipeline.addLast(ByteArrayEncoder())
                    pipeline.addLast(SocketChannelHandle(socketListener))
                }
            })
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val channelFuture = bootstrap.connect(host, port)
                    .addListener(object : ChannelFutureListener {
                        override fun operationComplete(channelFuture: ChannelFuture) {
                            isConnected = channelFuture.isSuccess
                            if (isConnected) {
                                channel = channelFuture.channel()
                            }
                        }
                    }).sync()
                // 等待连接关闭
                channelFuture.channel().closeFuture().sync()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.printStackTrace()
                    eventLoopGroup.shutdownGracefully()
                    isConnected = false

                    retryConnect()
                }
            }
        }
    }

    //重新连接
    private fun retryConnect() {
        if (isConnected) {
            return
        }
        if (retryTimes > 0) {
            retryTimes--
            SystemClock.sleep(retryInterval)
            Log.d(kTag, "retryConnect ===> retryTimes = $retryTimes")
            connect()
        }
    }

    fun sendData(bytes: ByteArray) {
        if (!isConnected) {
            return
        }
        channel.writeAndFlush(bytes).addListener(object : ChannelFutureListener {
            override fun operationComplete(future: ChannelFuture?) {
                future?.apply {
                    if (isSuccess) {
                        channel().close()
                        eventLoopGroup.shutdownGracefully()
                    }
                }
            }
        })
    }

    private val registry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return registry
    }
}