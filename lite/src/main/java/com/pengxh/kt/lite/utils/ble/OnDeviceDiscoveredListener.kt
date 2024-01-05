package com.pengxh.kt.lite.utils.ble

interface OnDeviceDiscoveredListener {
    fun onDeviceFound(device: BluetoothDevice) //搜索到设备

    fun onDiscoveryTimeout() //扫描超时
}