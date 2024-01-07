package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.adapter.MessageRecyclerAdapter
import com.pengxh.kt.lib.databinding.FragmentUtilsSocketBinding
import com.pengxh.kt.lib.model.MessageModel
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.timestampToTime
import com.pengxh.kt.lite.utils.WeakReferenceHandler
import com.pengxh.kt.lite.utils.socket.tcp.ConnectState
import com.pengxh.kt.lite.utils.socket.tcp.OnSocketListener
import com.pengxh.kt.lite.utils.socket.tcp.SocketClient

class SocketFragment : KotlinBaseFragment<FragmentUtilsSocketBinding>(), OnSocketListener,
    Handler.Callback {

    private val kTag = "SocketFragment"
    private val tcpClient by lazy { SocketClient(this) }
    private val messageArray: MutableList<MessageModel> = ArrayList()
    private val weakReferenceHandler by lazy { WeakReferenceHandler(this) }
    private lateinit var messageAdapter: MessageRecyclerAdapter

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsSocketBinding {
        return FragmentUtilsSocketBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        messageAdapter = MessageRecyclerAdapter(requireContext(), messageArray)
        binding.tcpRecyclerView.adapter = messageAdapter
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
            tcpClient.connectServer(strings[0], strings[1].toInt())
        }

        binding.sendTcpButton.setOnClickListener {
            val message = binding.tcpMsgInputView.text
            if (message.isNullOrBlank()) {
                return@setOnClickListener
            }

            messageArray.add(
                MessageModel(
                    System.currentTimeMillis().timestampToTime(),
                    message.toString()
                )
            )
            tcpClient.sendMessage(message.toString().toByteArray())
            weakReferenceHandler.sendEmptyMessage(0)
        }
    }

    /**
     * 处理接收消息
     * */
    override fun onMessageResponse(data: ByteArray?) {
        messageArray.add(
            MessageModel(
                System.currentTimeMillis().timestampToTime(),
                data.contentToString()
            )
        )
        weakReferenceHandler.sendEmptyMessage(0)
    }

    override fun onConnectStateChanged(state: ConnectState) {
        when (state) {
            ConnectState.SUCCESS -> {
                Log.d(kTag, "onConnectStatusChanged => 连接成功")
                binding.connectTcpButton.text = "断开"
            }

            ConnectState.CLOSED -> {
                Log.d(kTag, "onConnectStatusChanged => 连接关闭")
                binding.connectTcpButton.text = "连接"
            }

            else -> Log.d(kTag, "onConnectStatusChanged => 连接断开，正在重连")
        }
    }

    override fun handleMessage(msg: Message): Boolean {
        if (msg.what == 0) {
            binding.tcpRecyclerView.scrollToPosition(messageArray.size - 1)
            messageAdapter.notifyDataSetChanged()
        }
        return true
    }
}