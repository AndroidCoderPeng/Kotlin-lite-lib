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

class SocketChannelHandle(private val listener: OnSocketListener) :
    SimpleChannelInboundHandler<ByteArray>(), LifecycleOwner {

    private fun isOnMainThread(): Boolean {
        return Looper.getMainLooper().thread == Thread.currentThread()
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        if (isOnMainThread()) {
            listener.onConnectStateChanged(ConnectState.SUCCESS)
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                listener.onConnectStateChanged(ConnectState.SUCCESS)
            }
        }
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        super.channelInactive(ctx)
        if (isOnMainThread()) {
            listener.onConnectStateChanged(ConnectState.CLOSED)
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                listener.onConnectStateChanged(ConnectState.CLOSED)
            }
        }
    }

    override fun channelRead0(ctx: ChannelHandlerContext, data: ByteArray?) {
        if (isOnMainThread()) {
            listener.onMessageResponse(data)
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                listener.onMessageResponse(data)
            }
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        super.exceptionCaught(ctx, cause)
        if (isOnMainThread()) {
            listener.onConnectStateChanged(ConnectState.ERROR)
            ctx.close()
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                listener.onConnectStateChanged(ConnectState.ERROR)
                ctx.close()
            }
        }
    }

    private val registry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return registry
    }
}