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
    private var port = 8888
    private lateinit var listener: ISocketListener
    private var nioEventLoopGroup: NioEventLoopGroup? = null
    private var channel: Channel? = null
    private var reconnectNum = Int.MAX_VALUE
    private var isNeedReconnect = true
    private var isConnected = false
    private var reconnectInterval = 15000L

    override fun getLifecycle(): Lifecycle {
        return registry
    }

    /***建造者模式***开始*****************************************************************************/

    fun setHost(host: String): SocketClient {
        this.host = host
        return this
    }

    fun setPort(port: Int): SocketClient {
        this.port = port
        return this
    }

    fun setReconnectNum(reconnectNum: Int): SocketClient {
        this.reconnectNum = reconnectNum
        return this
    }

    fun setReconnectIntervalTime(reconnectInterval: Long): SocketClient {
        this.reconnectInterval = reconnectInterval
        return this
    }

    fun setSocketListener(socketListener: ISocketListener): SocketClient {
        this.listener = socketListener
        return this
    }

    fun connect() {
        if (isConnected) {
            disconnect()
            return
        }
        Log.d(kTag, "connect ===> 开始连接TCP服务器")
        isNeedReconnect = true
        reconnectNum = Int.MAX_VALUE
        connectServer()
    }

    /***建造者模式***结束*****************************************************************************/

    private fun connectServer() {
        var channelFuture: ChannelFuture? = null //连接管理对象
        if (!isConnected) {
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
        Log.d(kTag, "disconnect ===> 断开连接")
        nioEventLoopGroup?.shutdownGracefully()
        isNeedReconnect = false
        isConnected = false
    }

    //重新连接
    private fun reconnect() {
        if (isNeedReconnect && reconnectNum > 0 && !isConnected) {
            reconnectNum--
            SystemClock.sleep(reconnectInterval)
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