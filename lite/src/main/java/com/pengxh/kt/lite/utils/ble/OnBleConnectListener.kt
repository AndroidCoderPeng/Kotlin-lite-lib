package com.pengxh.kt.lite.utils.ble

import android.bluetooth.BluetoothGatt

interface OnBleConnectListener {
    fun onConnecting(bluetoothGatt: BluetoothGatt?) //正在连接

    fun onConnectSuccess(bluetoothGatt: BluetoothGatt?, status: Int) //连接成功

    fun onConnectFailure(bluetoothGatt: BluetoothGatt?, exception: String?, status: Int) //连接失败

    fun onDisConnecting(bluetoothGatt: BluetoothGatt?) //正在断开

    fun onDisConnectSuccess(bluetoothGatt: BluetoothGatt?, status: Int) // 断开连接

    fun onServiceDiscoverySucceed(bluetoothGatt: BluetoothGatt?, status: Int) //发现服务成功

    fun onServiceDiscoveryFailed(bluetoothGatt: BluetoothGatt?, msg: String?) //发现服务失败

    fun onReceiveMessage(bluetoothGatt: BluetoothGatt?, value: ByteArray) //收到消息

    fun onReceiveError(errorMsg: String?) //接收数据出错

    fun onWriteSuccess(bluetoothGatt: BluetoothGatt?, msg: ByteArray?) //写入成功

    fun onWriteFailure(bluetoothGatt: BluetoothGatt?, msg: ByteArray?, errorMsg: String?) //写入失败

    fun onReadRssi(bluetoothGatt: BluetoothGatt?, rssi: Int, status: Int) //成功读取到连接信号强度
}