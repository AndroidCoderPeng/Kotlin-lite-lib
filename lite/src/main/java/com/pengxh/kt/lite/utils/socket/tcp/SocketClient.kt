package com.pengxh.kt.lite.utils.socket.tcp

import android.os.SystemClock
import android.util.Log
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

class SocketClient {

    private val kTag = "SocketClient"
    private var host = ""
    private var port = 8888
    private var nioEventLoopGroup: NioEventLoopGroup? = null
    private var channel: Channel? = null
    private var listener: ISocketListener? = null

    //现在连接的状态
    var connectStatus = false //判断是否已连接
    private var reconnectNum = Int.MAX_VALUE //定义的重连到时候用
    private var isNeedReconnect = true //是否需要重连
    var isConnecting = false //是否正在连接
        private set
    private var reconnectIntervalTime: Long = 15000 //重连的时间

    //重连时间
    fun setReconnectNum(reconnectNum: Int) {
        this.reconnectNum = reconnectNum
    }

    fun setReconnectIntervalTime(reconnectIntervalTime: Long) {
        this.reconnectIntervalTime = reconnectIntervalTime
    }

    fun setSocketListener(listener: ISocketListener?) {
        this.listener = listener
    }

    fun connect(host: String, port: Int) {
        this.host = host
        this.port = port
        Log.d(kTag, "connect ===> 开始连接TCP服务器")
        if (isConnecting) {
            return
        }
        //起个线程
        val clientThread = object : Thread("client-Netty") {
            override fun run() {
                super.run()
                isNeedReconnect = true
                reconnectNum = Int.MAX_VALUE
                connectServer()
            }
        }
        clientThread.start()
    }

    private fun connectServer() {
        synchronized(this@SocketClient) {
            var channelFuture: ChannelFuture? = null //连接管理对象
            if (!connectStatus) {
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
                                    connectStatus = true
                                    channel = channelFuture.channel()
                                } else {
                                    Log.e(kTag, "operationComplete: 连接失败")
                                    connectStatus = false
                                }
                                isConnecting = false
                            }
                        }).sync()
                    // 等待连接关闭
                    channelFuture.channel().closeFuture().sync()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    connectStatus = false
                    listener?.onConnectStatusChanged(ConnectState.CLOSED)
                    channelFuture?.apply {
                        channel?.apply {
                            if (isOpen) {
                                close()
                            }
                        }
                    }
                    nioEventLoopGroup?.shutdownGracefully()
                    reconnect() //重新连接
                }
            }
        }
    }

    //断开连接
    fun disconnect() {
        Log.d(kTag, "disconnect ===> 断开连接")
        isNeedReconnect = false
        nioEventLoopGroup?.shutdownGracefully()
    }

    //重新连接
    private fun reconnect() {
        if (isNeedReconnect && reconnectNum > 0 && !connectStatus) {
            reconnectNum--
            SystemClock.sleep(reconnectIntervalTime)
            if (isNeedReconnect && reconnectNum > 0 && !connectStatus) {
                Log.d(kTag, "reconnect ===> 重新连接")
                connectServer()
            }
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