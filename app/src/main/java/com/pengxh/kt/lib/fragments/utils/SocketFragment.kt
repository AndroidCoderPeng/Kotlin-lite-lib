package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentUtilsSocketBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.utils.socket.tcp.ConnectState
import com.pengxh.kt.lite.utils.socket.tcp.OnSocketListener
import com.pengxh.kt.lite.utils.socket.tcp.SocketManager

class SocketFragment : KotlinBaseFragment<FragmentUtilsSocketBinding>(), OnSocketListener {

    private val kTag = "SocketFragment"
    private val socketManager by lazy { SocketManager(this) }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsSocketBinding {
        return FragmentUtilsSocketBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.connectTcpButton.setOnClickListener {
            val text = binding.tcpIpInputView.text
            if (text.isNullOrBlank()) {
                return@setOnClickListener
            }
            val address = text.toString()
            val strings = address.split(":")
            socketManager.connectServer(strings[0], strings[1].toInt())
        }
    }

    /**
     * 处理接收消息
     * */
    override fun onMessageResponse(data: ByteArray?) {

    }

    override fun onConnectStateChanged(state: ConnectState) {
        when (state) {
            ConnectState.SUCCESS -> Log.d(kTag, "onConnectStatusChanged => 连接成功")
            ConnectState.CLOSED -> Log.d(kTag, "onConnectStatusChanged => 连接关闭")
            else -> Log.d(kTag, "onConnectStatusChanged => 连接断开，正在重连")
        }
    }
}