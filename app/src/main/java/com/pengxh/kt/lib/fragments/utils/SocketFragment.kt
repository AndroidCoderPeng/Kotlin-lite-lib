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
import com.pengxh.kt.lite.utils.socket.tcp.OnTcpConnectStateCallback
import com.pengxh.kt.lite.utils.socket.tcp.TcpClient
import com.pengxh.kt.lite.utils.socket.udp.OnUdpMessageCallback
import com.pengxh.kt.lite.utils.socket.udp.UdpClient

class SocketFragment : KotlinBaseFragment<FragmentUtilsSocketBinding>(), OnTcpConnectStateCallback,
    OnUdpMessageCallback, Handler.Callback {

    private val kTag = "SocketFragment"
    private val udpClient by lazy { UdpClient(this) }
    private val tcpMessageArray: MutableList<MessageModel> = ArrayList()
    private val udpMessageArray: MutableList<MessageModel> = ArrayList()
    private val weakReferenceHandler by lazy { WeakReferenceHandler(this) }
    private lateinit var tcpAdapter: MessageRecyclerAdapter
    private lateinit var udpAdapter: MessageRecyclerAdapter
    private var tcpClient: TcpClient? = null
    private var type = 0

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsSocketBinding {
        return FragmentUtilsSocketBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        tcpAdapter = MessageRecyclerAdapter(requireContext(), tcpMessageArray)
        binding.tcpRecyclerView.adapter = tcpAdapter

        udpAdapter = MessageRecyclerAdapter(requireContext(), udpMessageArray)
        binding.udpRecyclerView.adapter = udpAdapter

        val text = binding.udpIpInputView.text
        if (text.isNullOrBlank()) {
            return
        }
        val address = text.toString()
        val strings = address.split(":")
        udpClient.bind(strings[0], strings[1].toInt())
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
            tcpClient = TcpClient(strings[0], strings[1].toInt(), this)
            tcpClient?.start()
        }

        binding.sendTcpButton.setOnClickListener {
            val message = binding.tcpMsgInputView.text
            if (message.isNullOrBlank()) {
                return@setOnClickListener
            }

            type = 0
            tcpMessageArray.add(
                MessageModel(
                    System.currentTimeMillis().timestampToTime(),
                    message.toString()
                )
            )
            tcpClient?.sendMessage(message.toString().toByteArray())
            weakReferenceHandler.sendEmptyMessage(0)
        }

        binding.sendUdpButton.setOnClickListener {
            val message = binding.udpMsgInputView.text
            if (message.isNullOrBlank()) {
                return@setOnClickListener
            }

            type = 1
            udpMessageArray.add(
                MessageModel(
                    System.currentTimeMillis().timestampToTime(),
                    message.toString()
                )
            )
            udpClient.sendMessage(message.toString())
            weakReferenceHandler.sendEmptyMessage(1)
        }
    }

    /**
     * 处理接收消息
     * */
    override fun onMessageReceived(data: ByteArray?) {
        tcpMessageArray.add(
            MessageModel(
                System.currentTimeMillis().timestampToTime(),
                data.contentToString()
            )
        )
        weakReferenceHandler.sendEmptyMessage(0)
    }

    override fun onConnected() {
        Log.d(kTag, "onConnectStatusChanged => 连接成功")
        binding.connectTcpButton.text = "断开"
    }

    override fun onDisconnected() {
        Log.d(kTag, "onConnectStatusChanged => 连接关闭")
        binding.connectTcpButton.text = "连接"
    }

    override fun onConnectFailed() {
        Log.d(kTag, "onConnectStatusChanged => 连接断开，正在重连")
    }

    override fun onReceivedUdpMessage(data: ByteArray) {
        udpMessageArray.add(
            MessageModel(
                System.currentTimeMillis().timestampToTime(),
                data.contentToString()
            )
        )
        weakReferenceHandler.sendEmptyMessage(1)
    }

    override fun handleMessage(msg: Message): Boolean {
        if (msg.what == 0) {
            binding.tcpRecyclerView.scrollToPosition(tcpMessageArray.size - 1)
            tcpAdapter.notifyDataSetChanged()
        } else if (msg.what == 1) {
            binding.udpRecyclerView.scrollToPosition(udpMessageArray.size - 1)
            udpAdapter.notifyDataSetChanged()
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tcpClient?.stop()
        udpClient.release()
    }
}