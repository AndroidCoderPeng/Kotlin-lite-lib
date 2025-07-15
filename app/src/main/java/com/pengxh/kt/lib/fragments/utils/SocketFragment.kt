package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.adapter.MessageRecyclerAdapter
import com.pengxh.kt.lib.databinding.FragmentUtilsSocketBinding
import com.pengxh.kt.lib.model.MessageModel
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.timestampToTime
import com.pengxh.kt.lite.utils.WeakReferenceHandler
import com.pengxh.kt.lite.utils.socket.tcp.OnStateChangedListener
import com.pengxh.kt.lite.utils.socket.tcp.TcpClient
import com.pengxh.kt.lite.utils.socket.udp.OnUdpMessageListener
import com.pengxh.kt.lite.utils.socket.udp.UdpClient
import com.pengxh.kt.lite.utils.socket.web.OnWebSocketListener
import com.pengxh.kt.lite.utils.socket.web.WebSocketClient
import okhttp3.Response
import okhttp3.WebSocket
import okio.ByteString

class SocketFragment : KotlinBaseFragment<FragmentUtilsSocketBinding>(), OnStateChangedListener,
    OnUdpMessageListener, OnWebSocketListener, Handler.Callback {

    private val kTag = "SocketFragment"
    private val udpClient by lazy { UdpClient(this) }
    private val tcpClient by lazy { TcpClient(this) }
    private val webSocketClient by lazy { WebSocketClient(this) }
    private val tcpMessageArray: MutableList<MessageModel> = ArrayList()
    private val udpMessageArray: MutableList<MessageModel> = ArrayList()
    private val weakReferenceHandler by lazy { WeakReferenceHandler(this) }
    private lateinit var tcpAdapter: MessageRecyclerAdapter
    private lateinit var udpAdapter: MessageRecyclerAdapter
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
        binding.socketButton.setOnClickListener {
            val text = binding.tcpIpInputView.text
            if (text.isNullOrBlank()) {
                return@setOnClickListener
            }
            val address = text.toString()
            val strings = address.split(":")
            if (tcpClient.isRunning()) {
                tcpClient.stop(false)
            } else {
                tcpClient.start(strings[0], strings[1].toInt())
            }
        }

        binding.sendTcpButton.setOnClickListener {
            val message = binding.tcpMsgInputView.text
            if (message.isNullOrBlank()) {
                return@setOnClickListener
            }

            type = 0
            tcpMessageArray.add(
                MessageModel(System.currentTimeMillis().timestampToTime(), message.toString())
            )
            tcpClient.send(message.toString().toByteArray())
            weakReferenceHandler.sendEmptyMessage(0)
        }

        binding.sendUdpButton.setOnClickListener {
            val message = binding.udpMsgInputView.text
            if (message.isNullOrBlank()) {
                return@setOnClickListener
            }

            type = 1
            udpMessageArray.add(
                MessageModel(System.currentTimeMillis().timestampToTime(), message.toString())
            )
            udpClient.sendMessage(message.toString())
            weakReferenceHandler.sendEmptyMessage(1)
        }

        binding.connectWebsocketButton.setOnClickListener {
            if (webSocketClient.isRunning()) {
                webSocketClient.stop()
            } else {
                val url = binding.websocketInputView.text
                if (url.isNullOrBlank()) {
                    return@setOnClickListener
                }
                webSocketClient.start("$url" + System.currentTimeMillis())
            }
        }
    }

    /**
     * 处理接收消息
     * */
    override fun onReceivedData(bytes: ByteArray?) {
        tcpMessageArray.add(
            MessageModel(System.currentTimeMillis().timestampToTime(), bytes.contentToString())
        )
        weakReferenceHandler.sendEmptyMessage(0)
    }

    override fun onConnected() {
        requireActivity().runOnUiThread { binding.socketButton.text = "断开" }
    }

    override fun onDisconnected() {
        requireActivity().runOnUiThread { binding.socketButton.text = "连接" }
    }

    override fun onConnectFailed() {
        requireActivity().runOnUiThread { binding.socketButton.text = "连接" }
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

    override fun onOpen(webSocket: WebSocket, response: Response) {
        requireActivity().runOnUiThread {
            binding.connectWebsocketButton.text = "断开"
        }
    }

    override fun onMessageResponse(webSocket: WebSocket, message: String) {

    }

    override fun onMessageResponse(webSocket: WebSocket, bytes: ByteString) {

    }

    override fun onServerDisconnected(webSocket: WebSocket, code: Int, reason: String) {
        requireActivity().runOnUiThread {
            binding.connectWebsocketButton.text = "连接"
        }
    }

    override fun onClientDisconnected(webSocket: WebSocket, code: Int, reason: String) {
        requireActivity().runOnUiThread {
            binding.connectWebsocketButton.text = "连接"
        }
    }

    override fun onFailure(webSocket: WebSocket?, t: Throwable?) {

    }

    override fun onMaxRetryReached() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        tcpClient.stop(false)
        udpClient.release()
    }
}