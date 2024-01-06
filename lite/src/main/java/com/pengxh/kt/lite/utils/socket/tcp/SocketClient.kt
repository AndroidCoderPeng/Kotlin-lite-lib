package com.pengxh.kt.lite.utils.socket.tcp

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
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

class SocketClient : LifecycleOwner {

    private val kTag = "SocketClient"
    private val registry = LifecycleRegistry(this)
    private var host = ""
    private var port = 0
    private var nioEventLoopGroup: NioEventLoopGroup? = null
    private var channel: Channel? = null
    private var retryTimes = 3
    private var isNeedReconnect = true
    private var isConnecting = false
    private var retryInterval = 10 * 1000L
    private lateinit var listener: OnSocketListener
    var isConnected = false

    override fun getLifecycle(): Lifecycle {
        return registry
    }

    fun setRetryTimes(retryTimes: Int) {
        this.retryTimes = retryTimes
    }

    fun setRetryInterval(retryInterval: Long) {
        this.retryInterval = retryInterval
    }

    fun setSocketListener(socketListener: OnSocketListener) {
        this.listener = socketListener
    }

    fun connect(host: String, port: Int) {
        this.host = host
        this.port = port
        if (isConnecting) {
            return
        }
        Log.d(kTag, "connect ===> 开始连接TCP服务器")
        isNeedReconnect = true
        synchronized(this@SocketClient) {
            connectServer()
        }
    }

    private fun connectServer() {
        var channelFuture: ChannelFuture? = null //连接管理对象
        if (!isConnected) {
            isConnecting = true
            nioEventLoopGroup = NioEventLoopGroup() //设置的连接group
            val bootstrap = Bootstrap()
            bootstrap.group(nioEventLoopGroup) //设置的一系列连接参数操作等
                .channel(NioSocketChannel::class.java)
                .option(ChannelOption.TCP_NODELAY, true) //无阻塞
                .option(ChannelOption.SO_KEEPALIVE, true) //长连接
                .option(
                    ChannelOption.RCVBUF_ALLOCATOR,
                    AdaptiveRecvByteBufAllocator(5000, 5000, 8000)
                ) //接收缓冲区 最小值太小时数据接收不全
                .handler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(channel: SocketChannel) {
                        val pipeline = channel.pipeline()
                        //参数1：代表读套接字超时的时间，没收到数据会触发读超时回调;
                        //参数2：代表写套接字超时时间，没进行写会触发写超时回调;
                        //参数3：将在未执行读取或写入时触发超时回调，0代表不处理;
                        //读超时尽量设置大于写超时，代表多次写超时时写心跳包，多次写了心跳数据仍然读超时代表当前连接错误，即可断开连接重新连接
                        pipeline.addLast(IdleStateHandler(60, 10, 0))
                        pipeline.addLast(ByteArrayDecoder())
                        pipeline.addLast(ByteArrayEncoder())
                        pipeline.addLast(SocketChannelHandle(listener))
                    }
                })
            try {
                //连接监听
                channelFuture = bootstrap.connect(host, port)
                    .addListener(object : ChannelFutureListener {
                        override fun operationComplete(channelFuture: ChannelFuture) {
                            if (channelFuture.isSuccess) {
                                channel = channelFuture.channel()
                                isConnected = true
                            } else {
                                isConnected = false
                            }
                            isConnecting = false
                        }
                    }).sync()
                // 等待连接关闭
                channelFuture.channel().closeFuture().sync()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                channelFuture?.apply {
                    channel?.apply {
                        if (isOpen) {
                            close()
                        }
                    }
                }
                nioEventLoopGroup?.shutdownGracefully()
                isConnected = false
                reconnect() //重新连接
            }
        }
    }

    //断开连接
    fun disconnect() {
        nioEventLoopGroup?.shutdownGracefully()
        isNeedReconnect = false
        isConnected = false
    }

    //重新连接
    private fun reconnect() {
        if (isNeedReconnect && retryTimes > 0 && !isConnected) {
            retryTimes--
            SystemClock.sleep(retryInterval)
            Log.d(kTag, "reconnect ===> 重新连接")
            connectServer()
        }
    }

    fun sendData(bytes: ByteArray) {
        channel?.writeAndFlush(bytes)?.addListener(ChannelFutureListener { future ->
            if (!future.isSuccess) {
                future.channel().close()
                nioEventLoopGroup!!.shutdownGracefully()
            }
        })
    }
}