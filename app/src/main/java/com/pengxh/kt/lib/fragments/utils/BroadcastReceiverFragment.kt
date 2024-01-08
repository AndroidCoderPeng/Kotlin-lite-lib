package com.pengxh.kt.lib.fragments.utils

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentUtilsBroadcastBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.utils.BroadcastManager

class BroadcastReceiverFragment : KotlinBaseFragment<FragmentUtilsBroadcastBinding>() {

    private val kTag = "BroadcastReceiverFragment"
    private val ACTION_AP_WIFI_CHANGED = "android.net.wifi.WIFI_AP_STATE_CHANGED"
    private val broadcastManager by lazy { BroadcastManager(requireContext()) }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsBroadcastBinding {
        return FragmentUtilsBroadcastBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        broadcastManager.addAction(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val action = intent?.action
                    action?.apply {
                        if (this == BluetoothAdapter.ACTION_STATE_CHANGED) {
                            when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)) {
                                BluetoothAdapter.STATE_ON -> {
                                    binding.bleStateView.setBackgroundColor(Color.GREEN)
                                }

                                BluetoothAdapter.STATE_OFF -> {
                                    binding.bleStateView.setBackgroundColor(Color.RED)
                                }
                            }
                        }
                    }
                }
            }, BluetoothAdapter.ACTION_STATE_CHANGED
        )

        broadcastManager.addAction(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val action = intent?.action
                    action?.apply {
                        if (this == ACTION_AP_WIFI_CHANGED) {
                            //便携式热点的状态为：10---正在关闭；11---已关闭；12---正在开启；13---已开启
                            when (intent.getIntExtra("wifi_state", 0)) {
                                13 -> {
                                    binding.wifiStateView.setBackgroundColor(Color.GREEN)
                                }

                                11 -> {
                                    binding.wifiStateView.setBackgroundColor(Color.RED)
                                }
                            }
                        } else if (this == Intent.ACTION_POWER_CONNECTED) {
                            binding.chargeStateView.setBackgroundColor(Color.GREEN)
                        } else if (this == Intent.ACTION_POWER_DISCONNECTED) {
                            binding.chargeStateView.setBackgroundColor(Color.RED)
                        }
                    }
                }
            },
            ACTION_AP_WIFI_CHANGED,
            Intent.ACTION_POWER_CONNECTED,
            Intent.ACTION_POWER_DISCONNECTED
        )
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }

    override fun onDestroy() {
        super.onDestroy()
        broadcastManager.destroy(
            BluetoothAdapter.ACTION_STATE_CHANGED,
            ACTION_AP_WIFI_CHANGED,
            Intent.ACTION_POWER_CONNECTED,
            Intent.ACTION_POWER_DISCONNECTED
        )
    }
}