package com.pengxh.kt.lib.fragments.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentUtilsBleBinding
import com.pengxh.kt.lib.utils.LocaleConstant
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.convertColor
import com.pengxh.kt.lite.utils.LoadingDialogHub
import com.pengxh.kt.lite.utils.ble.BleDeviceManager
import com.pengxh.kt.lite.utils.ble.OnDeviceConnectListener
import com.pengxh.kt.lite.utils.ble.OnDeviceDiscoveredListener
import com.pengxh.kt.lite.widget.dialog.BottomActionSheet

@SuppressLint("all")
class BleFragment : KotlinBaseFragment<FragmentUtilsBleBinding>() {

    private val kTag = "BleFragment"
    private val bleDeviceManager by lazy { BleDeviceManager(requireContext()) }
    private val bluetoothDevices = ArrayList<BluetoothDevice>()
    private var isConnected = false

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsBleBinding {
        return FragmentUtilsBleBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun onResume() {
        super.onResume()
        if (bleDeviceManager.isBluetoothEnabled()) {
            binding.openButton.isEnabled = false
            binding.bleStateView.setBackgroundColor(Color.GREEN)
        } else {
            binding.openButton.isEnabled = true
            binding.bleStateView.setBackgroundColor(Color.RED)
        }
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        binding.openButton.setOnClickListener {
            bleDeviceManager.openBluetooth()
        }
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.scanButton.setOnClickListener {
            if (bleDeviceManager.isDiscovering() || isConnected) {
                bleDeviceManager.stopDiscoverDevice()
            }

            LoadingDialogHub.show(requireActivity(), "设备搜索中...")
            bleDeviceManager.startScanDevice(object : OnDeviceDiscoveredListener {
                override fun onDeviceFound(device: BluetoothDevice) {
                    if (bluetoothDevices.isEmpty()) {
                        bluetoothDevices.add(device)
                    } else {
                        //0表示未添加到list的新设备，1表示已经扫描并添加到list的设备
                        var judge = 0
                        for (temp in bluetoothDevices) {
                            if (temp.address == device.address) {
                                judge = 1
                                break
                            }
                        }
                        if (judge == 0) {
                            bluetoothDevices.add(device)
                        }
                    }
                }

                override fun onDeviceDiscoveryEnd() {
                    LoadingDialogHub.dismiss()
                    //显示搜索到设备列表
                    val bleArray = ArrayList<String>()
                    bluetoothDevices.forEach {
                        bleArray.add(it.name)
                    }

                    BottomActionSheet.Builder()
                        .setContext(requireContext())
                        .setActionItemTitle(bleArray)
                        .setItemTextColor(R.color.mainColor.convertColor(requireContext()))
                        .setOnActionSheetListener(object : BottomActionSheet.OnActionSheetListener {
                            override fun onActionItemClick(position: Int) {
                                //连接点击的设备
                                startConnectDevice(bluetoothDevices[position])
                            }
                        }).build().show()
                }

            }, 10 * 1000)
        }
    }

    private fun startConnectDevice(device: BluetoothDevice) {
        // 当前蓝牙设备
        if (!isConnected) {
            LoadingDialogHub.show(requireActivity(), "正在连接...")
            bleDeviceManager.connectBleDevice(
                device,
                LocaleConstant.SERVICE_UUID, LocaleConstant.WRITE_CHARACTERISTIC_UUID,
                10 * 1000,
                bleConnectListener
            )
        } else {
            bleDeviceManager.disConnectDevice()
        }
    }

    private val bleConnectListener = object : OnDeviceConnectListener {
        override fun onConnecting(bluetoothGatt: BluetoothGatt?) {
            Log.d(kTag, "onConnecting => ")
        }

        override fun onConnectSuccess(bluetoothGatt: BluetoothGatt?, status: Int) {
            Log.d(kTag, "onConnectSuccess => ")
        }

        override fun onConnectFailure(
            bluetoothGatt: BluetoothGatt?, exception: String, status: Int
        ) {
            Log.d(kTag, "onConnectFailure => ")
            isConnected = false
        }

        override fun onDisConnecting(bluetoothGatt: BluetoothGatt?) {
            Log.d(kTag, "onDisConnecting => ")
        }

        override fun onDisConnectSuccess(bluetoothGatt: BluetoothGatt?, status: Int) {
            Log.d(kTag, "onDisConnectSuccess => ")
            isConnected = false
        }

        override fun onServiceDiscoverySuccess(bluetoothGatt: BluetoothGatt?, status: Int) {
            LoadingDialogHub.dismiss()
            isConnected = true
            //可以发送指令
            bleDeviceManager.sendCommand(byteArrayOf())
        }

        override fun onServiceDiscoveryFailed(bluetoothGatt: BluetoothGatt?, msg: String) {
            Log.d(kTag, "onServiceDiscoveryFailed => ")
            isConnected = false
        }

        override fun onReceiveMessage(bluetoothGatt: BluetoothGatt?, value: ByteArray) {
            //解析返回值
            Log.d(kTag, "onReceiveMessage => ${value.contentToString()}")
        }

        override fun onReceiveError(errorMsg: String) {
            Log.d(kTag, "onReceiveError => $errorMsg")
        }

        override fun onWriteSuccess(bluetoothGatt: BluetoothGatt?, msg: ByteArray?) {
            Log.d(kTag, "onWriteSuccess => ${msg.contentToString()}")
        }

        override fun onWriteFailed(bluetoothGatt: BluetoothGatt?, errorMsg: String) {
            Log.d(kTag, "onWriteFailure => $errorMsg")
        }

        override fun onReadRssi(bluetoothGatt: BluetoothGatt?, rssi: Int, status: Int) {
            Log.d(kTag, "onReadRssi => ")
        }
    }
}