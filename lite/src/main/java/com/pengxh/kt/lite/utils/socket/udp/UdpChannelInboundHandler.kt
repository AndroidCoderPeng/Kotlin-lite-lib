package com.pengxh.kt.lite.utils.socket.udp

import android.os.Looper
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.socket.DatagramPacket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UdpChannelInboundHandler(private val messageCallback: OnUdpMessageCallback) :
    SimpleChannelInboundHandler<DatagramPacket>(), LifecycleOwner {

    private val kTag = "UdpChannelInboundHandler"

    private fun isOnMainThread(): Boolean {
        return Looper.getMainLooper().thread == Thread.currentThread()
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        super.channelInactive(ctx)
        ctx?.close()
        Log.d(kTag, "channelInactive: 连接关闭")
    }

    override fun channelRead0(ctx: ChannelHandlerContext, datagramPacket: DatagramPacket) {
        val byteBuf = datagramPacket.content()
        val byteArray = ByteArray(byteBuf.readableBytes())
        byteBuf.readBytes(byteArray)
        if (isOnMainThread()) {
            messageCallback.onReceivedUdpMessage(byteArray)
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                messageCallback.onReceivedUdpMessage(byteArray)
            }
        }
    }

    private val registry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return registry
    }
}