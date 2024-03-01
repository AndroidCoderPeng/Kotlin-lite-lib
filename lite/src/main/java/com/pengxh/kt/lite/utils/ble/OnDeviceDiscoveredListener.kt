package com.pengxh.kt.lite.utils.ble

import android.bluetooth.BluetoothDevice

interface OnDeviceDiscoveredListener {
    fun onDeviceFound(device: BluetoothDevice) //搜索到设备

    fun onDeviceDiscoveryEnd() //扫描结束
}