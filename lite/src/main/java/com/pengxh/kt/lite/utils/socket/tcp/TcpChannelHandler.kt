package com.pengxh.kt.lite.utils.socket.tcp

import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TcpChannelHandler(private val messageCallback: OnTcpMessageCallback) :
    SimpleChannelInboundHandler<ByteArray>(), LifecycleOwner {

    private fun isOnMainThread(): Boolean {
        return Looper.getMainLooper().thread == Thread.currentThread()
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        if (isOnMainThread()) {
            messageCallback.onConnectStateChanged(ConnectState.SUCCESS)
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                messageCallback.onConnectStateChanged(ConnectState.SUCCESS)
            }
        }
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        super.channelInactive(ctx)
        if (isOnMainThread()) {
            messageCallback.onConnectStateChanged(ConnectState.CLOSED)
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                messageCallback.onConnectStateChanged(ConnectState.CLOSED)
            }
        }
    }

    override fun channelRead0(ctx: ChannelHandlerContext, data: ByteArray?) {
        if (isOnMainThread()) {
            messageCallback.onReceivedTcpMessage(data)
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                messageCallback.onReceivedTcpMessage(data)
            }
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        super.exceptionCaught(ctx, cause)
        if (isOnMainThread()) {
            messageCallback.onConnectStateChanged(ConnectState.ERROR)
            ctx.close()
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                messageCallback.onConnectStateChanged(ConnectState.ERROR)
                ctx.close()
            }
        }
    }

    private val registry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return registry
    }
}