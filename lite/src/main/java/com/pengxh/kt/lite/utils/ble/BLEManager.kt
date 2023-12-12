package com.pengxh.kt.lite.utils.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import com.pengxh.kt.lite.extensions.getSystemService
import com.pengxh.kt.lite.utils.Constant
import java.util.UUID


/**
 * 1、扫描设备
 * 2、配对设备
 * 3、解除设备配对
 * 4、连接设备
 * 6、发现服务
 * 7、打开读写功能
 * 8、数据通讯（发送数据、接收数据）
 * 9、断开连接
 */
@SuppressLint("MissingPermission", "StaticFieldLeak")
object BLEManager {
    private const val kTag = "BLEManager"
    private val bleHandler = Handler(Looper.getMainLooper())
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var discoveredListener: OnDeviceDiscoveredListener
    private lateinit var serviceUuid: UUID
    private lateinit var readUuid: UUID
    private lateinit var writeUuid: UUID
    private lateinit var bleConnectListener: OnBleConnectListener
    private lateinit var bluetoothGatt: BluetoothGatt
    private lateinit var readCharacteristic: BluetoothGattCharacteristic
    private lateinit var writeCharacteristic: BluetoothGattCharacteristic
    private lateinit var context: Context
    private var isConnecting = false

    /**
     * 初始化BLE
     */
    fun initBLE(context: Context): Boolean {
        this.context = context
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            val bluetoothManager = context.getSystemService<BluetoothManager>()!!
            bluetoothAdapter = bluetoothManager.adapter
            true
        } else {
            false
        }
    }

    /**
     * 蓝牙是否可用
     */
    fun isBluetoothEnable(): Boolean {
        return bluetoothAdapter.isEnabled
    }

    /**
     * 打开蓝牙
     *
     * @param isDirectly true 直接打开蓝牙  false 提示用户打开
     */
    fun openBluetooth(isDirectly: Boolean) {
        if (!isBluetoothEnable()) {
            if (isDirectly) {
                Log.d(kTag, "直接打开手机蓝牙")
                bluetoothAdapter.enable()
            } else {
                context.startActivity(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
        } else {
            Log.d(kTag, "手机蓝牙状态已开")
        }
    }

    /**
     * 本地蓝牙是否处于正在扫描状态
     * @return true false
     */
    fun isDiscovery(): Boolean {
        return bluetoothAdapter.isDiscovering
    }

    /**
     * 开始扫描设备
     */
    fun startDiscoverDevice(listener: OnDeviceDiscoveredListener, scanTime: Long) {
        this.discoveredListener = listener
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bluetoothAdapter.bluetoothLeScanner.startScan(scanCallback)
        }
        //设定最长扫描时间
        bleHandler.postDelayed(stopDiscoverRunnable, scanTime)
    }

    /**
     * 停止扫描设备
     */
    fun stopDiscoverDevice() {
        bleHandler.removeCallbacks(stopDiscoverRunnable)
    }

    private val stopDiscoverRunnable = Runnable {
        discoveredListener.onDiscoveryTimeout()
        //scanTime之后还没有扫描到设备，就停止扫描。
        bluetoothAdapter.bluetoothLeScanner.stopScan(scanCallback)
    }

    /**
     * 扫描设备回调
     */
    private val scanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            if (result == null) {
                return
            }
            val device = result.device
            if (device.name != null || !TextUtils.isEmpty(device.name)) {
                discoveredListener.onDeviceFound(BlueToothBean(device, result.rssi))
            }
        }

        override fun onBatchScanResults(results: List<ScanResult>) {

        }

        override fun onScanFailed(errorCode: Int) {
            Log.d(kTag, "onScanFailed: errorCode ===> $errorCode")
        }
    }

    fun connectBleDevice(
        context: Context, bluetoothDevice: BluetoothDevice,
        outTime: Long, serviceUUID: String,
        readUUID: String, writeUUID: String,
        onBleConnectListener: OnBleConnectListener
    ) {
        if (isConnecting) {
            Log.d(kTag, "connectBleDevice() ===> isConnecting = true")
            return
        }
        this.serviceUuid = UUID.fromString(serviceUUID)
        this.readUuid = UUID.fromString(readUUID)
        this.writeUuid = UUID.fromString(writeUUID)
        this.bleConnectListener = onBleConnectListener
        Log.d(kTag, "开始准备连接：" + bluetoothDevice.name + "-->" + bluetoothDevice.address)
        try {
            bluetoothGatt = bluetoothDevice.connectGatt(context, false, bluetoothGattCallback)
            bluetoothGatt.connect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //设置连接超时时间10s
        bleHandler.postDelayed(connectOutTimeRunnable, outTime)
    }

    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            val bluetoothDevice = gatt?.device
            Log.d(kTag, "连接的设备：" + bluetoothDevice?.name + "  " + bluetoothDevice?.address)
            isConnecting = true
            //移除连接超时
            bleHandler.removeCallbacks(connectOutTimeRunnable)
            when (newState) {
                BluetoothGatt.STATE_CONNECTING -> {
                    Log.d(kTag, "正在连接...")
                    bleConnectListener.onConnecting(gatt)  //正在连接回调
                }

                BluetoothGatt.STATE_CONNECTED -> {
                    Log.d(kTag, "连接成功")
                    //连接成功去发现服务
                    gatt?.discoverServices()
                    //设置发现服务超时时间
                    bleHandler.postDelayed(
                        serviceDiscoverOutTimeRunnable,
                        Constant.MAX_CONNECT_TIME
                    )
                    bleConnectListener.onConnectSuccess(gatt, status)
                }

                BluetoothGatt.STATE_DISCONNECTING -> {
                    Log.d(kTag, "正在断开...")
                    bleConnectListener.onDisConnecting(gatt) //正在断开回调
                }

                BluetoothGatt.STATE_DISCONNECTED -> {
                    Log.d(kTag, "断开连接status: $status")
                    gatt?.close()
                    isConnecting = false
                    when (status) {
                        133 -> {//133连接异常,无法连接
                            gatt?.close()
                            bleConnectListener.onConnectFailure(
                                gatt, "连接异常！", status
                            )
                            Log.d(
                                kTag,
                                "连接失败status：" + status + "  " + bluetoothDevice?.address
                            )
                        }

                        62 -> {//62没有发现服务 异常断开
                            gatt?.close()
                            bleConnectListener.onConnectFailure(
                                gatt, "连接成功服务未发现断开！", status
                            )
                        }

                        else -> {
                            //0:正常断开
                            //8:因为距离远或者电池无法供电断开连接
                            //34:断开
                            //其他断开
                            bleConnectListener.onDisConnectSuccess(gatt, status)
                        }
                    }
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            isConnecting = false
            //移除发现服务超时
            bleHandler.removeCallbacks(serviceDiscoverOutTimeRunnable)
            //配置服务信息
            if (setupService(gatt!!, serviceUuid, readUuid, writeUuid)) {
                //成功发现服务回调
                bleConnectListener.onServiceDiscoverySucceed(gatt, status)
            } else {
                bleConnectListener.onServiceDiscoveryFailed(gatt, "获取服务特征异常")
            }
        }

        //读取蓝牙设备发出来的数据回调
        override fun onCharacteristicRead(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
            Log.d(kTag, "onCharacteristicRead: $status")
        }

        //向蓝牙设备写入数据结果回调
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?, status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            if (characteristic?.value == null) {
                Log.e(kTag, "characteristic.getValue() == null");
                return
            }
            //将收到的字节数组转换成十六进制字符串
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    //写入成功
                    bleConnectListener.onWriteSuccess(gatt, characteristic.value)
                }

                BluetoothGatt.GATT_FAILURE -> {
                    //写入失败
                    bleConnectListener.onWriteFailure(gatt, characteristic.value, "写入失败")
                }

                BluetoothGatt.GATT_WRITE_NOT_PERMITTED -> {
                    Log.d(kTag, "没有权限")
                }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
            //接收数据
            Log.d(kTag, "收到数据:" + characteristic?.value!!.toList())
            bleConnectListener.onReceiveMessage(gatt, characteristic)  //接收数据回调
        }

        override fun onDescriptorRead(
            gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int
        ) {
            super.onDescriptorRead(gatt, descriptor, status)
            //开启监听成功，可以从设备读数据了
            Log.d(kTag, "onDescriptorRead开启监听成功")
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)
            //开启监听成功，可以向设备写入命令了
            Log.d(kTag, "onDescriptorWrite开启监听成功")
        }

        //蓝牙信号强度
        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            super.onReadRemoteRssi(gatt, rssi, status)
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    Log.w(kTag, "读取RSSI值成功，RSSI值: $rssi ,status: $status")
                    bleConnectListener.onReadRssi(gatt, rssi, status)  //成功读取连接的信号强度回调
                }

                BluetoothGatt.GATT_FAILURE -> Log.w(kTag, "读取RSSI值失败，status: $status")
            }
        }
    }

    private val connectOutTimeRunnable = Runnable {
        isConnecting = false
        bluetoothGatt.disconnect()
        //连接超时当作连接失败回调
        bleConnectListener.onConnectFailure(
            bluetoothGatt, "连接超时", -1
        )  //连接失败回调
    }

    private val serviceDiscoverOutTimeRunnable = Runnable {
        isConnecting = false
        bluetoothGatt.disconnect()
        //发现服务超时当作连接失败回调
        bleConnectListener.onConnectFailure(
            bluetoothGatt, "发现服务超时！", -1
        )  //连接失败回调
    }

    private fun setupService(
        bluetoothGatt: BluetoothGatt, serviceUUID: UUID, readUUID: UUID, writeUUID: UUID
    ): Boolean {
        var notifyCharacteristic: BluetoothGattCharacteristic? = null
        bluetoothGatt.services.forEach { service ->
            if (service.uuid == serviceUUID) {
                service.characteristics.forEach { characteristic ->
                    val charaProp = characteristic.properties
                    if (characteristic.uuid == readUUID) {  //读特征
                        //读特征
                        readCharacteristic = characteristic
                    }
                    if (characteristic.uuid == writeUUID) {  //写特征
                        writeCharacteristic = characteristic
                    }
                    if (charaProp and BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
                        val notifyServiceUUID = service.uuid
                        val notifyCharacteristicUUID = characteristic.uuid
                        Log.d(
                            kTag,
                            "notifyCharacteristicUUID = $notifyCharacteristicUUID, notifyServiceUUID = $notifyServiceUUID"
                        )
                        notifyCharacteristic = bluetoothGatt.getService(notifyServiceUUID)
                            .getCharacteristic(notifyCharacteristicUUID)
                    }
                }
            }
        }
        //打开读通知，打开的是notifyCharacteristic！！！，不然死活不走onCharacteristicChanged回调
        bluetoothGatt.setCharacteristicNotification(notifyCharacteristic, true)
        //一定要重新设置
        for (descriptor in notifyCharacteristic!!.descriptors) {
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            bluetoothGatt.writeDescriptor(descriptor)
        }
        //延迟2s，保证所有通知都能及时打开
        bleHandler.postDelayed({ }, 2000)
        return true
    }

    fun sendCommand(cmd: ByteArray) {
        val value = writeCharacteristic.setValue(cmd)
        Log.d(kTag, "写特征设置值结果：$value")
        bluetoothGatt.writeCharacteristic(writeCharacteristic)
    }

    fun disConnectDevice() {
        bluetoothGatt.disconnect()
        isConnecting = false
    }
}