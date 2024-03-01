package com.pengxh.kt.lite.utils.ble

import android.Manifest
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
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.core.app.ActivityCompat
import com.pengxh.kt.lite.extensions.getSystemService
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.utils.Constant
import com.pengxh.kt.lite.utils.WeakReferenceHandler
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
class BleDeviceManager(private val context: Context) : Handler.Callback {
    private val kTag = "BleDeviceManager"
    private val weakReferenceHandler by lazy { WeakReferenceHandler(this) }
    private var bluetoothAdapter: BluetoothAdapter
    private var isConnecting = false
    private lateinit var bleDiscoveryListener: OnDeviceDiscoveredListener
    private lateinit var bleConnectListener: OnDeviceConnectListener
    private lateinit var serviceUuid: UUID
    private lateinit var writeUuid: UUID
    private lateinit var bluetoothGatt: BluetoothGatt
    private lateinit var writeCharacteristic: BluetoothGattCharacteristic

    override fun handleMessage(msg: Message): Boolean {
        return true
    }

    init {
        val bluetoothManager = context.getSystemService<BluetoothManager>()!!
        bluetoothAdapter = bluetoothManager.adapter
    }

    /**
     * 打开蓝牙
     */
    fun openBluetooth() {
        if (!bluetoothAdapter.isEnabled) {
            if (checkConnectPermission()) {
                context.startActivity(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
        }
    }

    /**
     * 蓝牙是否已打开
     */
    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter.isEnabled
    }

    /**
     * 本地蓝牙是否处于正在扫描状态
     * @return true false
     */
    fun isDiscovering(): Boolean {
        return if (checkScanPermission()) {
            bluetoothAdapter.isDiscovering
        } else false;
    }

    /**
     * 开始扫描设备
     */
    fun startScanDevice(listener: OnDeviceDiscoveredListener, scanTime: Long) {
        this.bleDiscoveryListener = listener
        if (checkScanPermission()) {
            bluetoothAdapter.bluetoothLeScanner.startScan(scanCallback)
            //设定最长扫描时间
            weakReferenceHandler.postDelayed(stopScanRunnable, scanTime)
        }
    }

    /**
     * 停止扫描设备
     */
    fun stopDiscoverDevice() {
        weakReferenceHandler.removeCallbacks(stopScanRunnable)
    }

    @SuppressLint("MissingPermission")
    private val stopScanRunnable = Runnable {
        bleDiscoveryListener.onDeviceDiscoveryEnd()
        //scanTime之后还没有扫描到设备，就停止扫描。
        if (checkScanPermission()) {
            bluetoothAdapter.bluetoothLeScanner.stopScan(scanCallback)
        }
    }

    /**
     * 扫描设备回调
     */
    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            result?.apply {
                if (checkConnectPermission()) {
                    if (!device.name.isNullOrBlank()) {
                        bleDiscoveryListener.onDeviceFound(device)
                    }
                }
            }
        }

        override fun onBatchScanResults(results: List<ScanResult>) {

        }

        override fun onScanFailed(errorCode: Int) {
            Log.d(kTag, "onScanFailed: errorCode ===> $errorCode")
        }
    }

    fun connectBleDevice(
        bluetoothDevice: BluetoothDevice, serviceUuid: String, writeUuid: String, outTime: Long,
        connectListener: OnDeviceConnectListener
    ) {
        if (isConnecting) {
            Log.d(kTag, "connectBleDevice() ===> isConnecting = true")
            return
        }
        this.serviceUuid = UUID.fromString(serviceUuid)
        this.writeUuid = UUID.fromString(writeUuid)
        this.bleConnectListener = connectListener
        if (checkConnectPermission()) {
            Log.d(kTag, "开始准备连接：" + bluetoothDevice.name + "-->" + bluetoothDevice.address)
            try {
                bluetoothGatt = bluetoothDevice.connectGatt(context, false, bluetoothGattCallback)
                bluetoothGatt.connect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //设置连接超时时间
            weakReferenceHandler.postDelayed(connectTimeoutRunnable, outTime)
        }
    }

    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if (checkConnectPermission()) {
                gatt?.apply {
                    Log.d(kTag, "连接的设备：" + this.device.name + "  " + this.device.address)
                    isConnecting = true
                    //移除连接超时
                    weakReferenceHandler.removeCallbacks(connectTimeoutRunnable)
                    when (newState) {
                        BluetoothGatt.STATE_CONNECTING -> {
                            Log.d(kTag, "正在连接...")
                            bleConnectListener.onConnecting(this)  //正在连接回调
                        }

                        BluetoothGatt.STATE_CONNECTED -> {
                            Log.d(kTag, "连接成功")
                            //连接成功去发现服务
                            discoverServices()
                            //设置发现服务超时时间
                            weakReferenceHandler.postDelayed(
                                discoverServiceTimeoutRunnable, Constant.MAX_CONNECT_TIME
                            )
                            bleConnectListener.onConnectSuccess(this, status)
                        }

                        BluetoothGatt.STATE_DISCONNECTING -> {
                            Log.d(kTag, "正在断开...")
                            bleConnectListener.onDisConnecting(this) //正在断开回调
                        }

                        BluetoothGatt.STATE_DISCONNECTED -> {
                            when (status) {
                                133 -> {//133连接异常,无法连接
                                    bleConnectListener.onConnectFailure(this, "连接异常！", status)
                                    Log.d(kTag, "${this.device.address}连接失败")
                                }

                                62 -> {//62没有发现服务 异常断开
                                    bleConnectListener.onConnectFailure(
                                        this, "没有发现服务，异常断开！", status
                                    )
                                }

                                else -> {
                                    //0:正常断开
                                    //8:因为距离远或者电池无法供电断开连接
                                    //34:断开
                                    //其他断开
                                    Log.d(kTag, "断开连接，status = $status")
                                    bleConnectListener.onDisConnectSuccess(this, status)
                                }
                            }
                            close()
                            isConnecting = false
                        }
                    }
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            isConnecting = false
            //移除发现超时服务
            weakReferenceHandler.removeCallbacks(discoverServiceTimeoutRunnable)
            //配置服务信息
            gatt?.apply {
                if (configBleService(this, serviceUuid, writeUuid)) {
                    //成功发现服务回调
                    bleConnectListener.onServiceDiscoverySuccess(this, status)
                } else {
                    bleConnectListener.onServiceDiscoveryFailed(this, "获取服务特征异常")
                }
            }

        }

        //读取蓝牙设备发出来的数据回调
        override fun onCharacteristicRead(
            gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, value, status)
            Log.d(kTag, "onCharacteristicRead => $status")
        }

        //向蓝牙设备写入数据结果回调
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            //将收到的字节数组转换成十六进制字符串
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    bleConnectListener.onWriteSuccess(gatt, characteristic?.value)
                }

                BluetoothGatt.GATT_FAILURE -> bleConnectListener.onWriteFailed(gatt, "写入失败")

                BluetoothGatt.GATT_WRITE_NOT_PERMITTED -> "没有写入设备权限".show(context)
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray
        ) {
            super.onCharacteristicChanged(gatt, characteristic, value)
            //接收数据
            Log.d(kTag, "收到数据:" + value.toList())
            bleConnectListener.onReceiveMessage(gatt, value)  //接收数据回调
        }

        override fun onDescriptorRead(
            gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int, value: ByteArray
        ) {
            super.onDescriptorRead(gatt, descriptor, status, value)
            Log.d(kTag, "onDescriptorRead => 开启监听成功，可以读取设备")
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)
            //开启监听成功，可以向设备写入命令了
            Log.d(kTag, "onDescriptorWrite => 开启监听成功，可以写入设备")
        }

        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            super.onReadRemoteRssi(gatt, rssi, status)
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    Log.d(kTag, "onReadRemoteRssi => RSSI值: $rssi")
                    bleConnectListener.onReadRssi(gatt, rssi, status)  //成功读取连接的信号强度回调
                }

                BluetoothGatt.GATT_FAILURE -> Log.d(kTag, "读取RSSI值失败，status: $status")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private val connectTimeoutRunnable = Runnable {
        isConnecting = false
        if (checkConnectPermission()) {
            bluetoothGatt.disconnect()
            //连接超时当作连接失败回调
            bleConnectListener.onConnectFailure(bluetoothGatt, "连接超时", -1)
        }
    }

    @SuppressLint("MissingPermission")
    private val discoverServiceTimeoutRunnable = Runnable {
        isConnecting = false
        if (checkConnectPermission()) {
            bluetoothGatt.disconnect()
            //发现服务超时当作连接失败回调
            bleConnectListener.onConnectFailure(bluetoothGatt, "发现服务超时", -1)
        }
    }

    private fun configBleService(gatt: BluetoothGatt, serviceUuid: UUID, writeUuid: UUID): Boolean {
        var notifyCharacteristic: BluetoothGattCharacteristic? = null
        gatt.services.forEach { service ->
            Log.d(kTag, "configBleService => ${service.uuid}")
            if (service.uuid == serviceUuid) {
                service.characteristics.forEach { characteristic ->
                    val charaProp = characteristic.properties

                    if (characteristic.uuid === writeUuid) {
                        //写特征
                        writeCharacteristic = characteristic
                    }

                    if (charaProp and BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
                        val notifyServiceUuid = service.uuid
                        val notifyCharacteristicUuid = characteristic.uuid
                        notifyCharacteristic = bluetoothGatt.getService(notifyServiceUuid)
                            .getCharacteristic(notifyCharacteristicUuid)
                    }
                }
            } else {
                Log.d(kTag, "configBleService => 未匹配到uuid")
            }
        }
        //打开读通知，打开的是notifyCharacteristic，不然不走onCharacteristicChanged回调
        if (checkConnectPermission()) {
            bluetoothGatt.setCharacteristicNotification(notifyCharacteristic, true)
            //一定要重新设置
            notifyCharacteristic?.apply {
                descriptors.forEach {
                    it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    bluetoothGatt.writeDescriptor(it)
                }
            }
            //延迟2s，保证所有通知都能及时打开
            weakReferenceHandler.postDelayed({ }, 2000)
            return true
        }
        return false
    }

    fun sendCommand(cmd: ByteArray) {
        if (checkConnectPermission()) {
            val value: Any
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                value = bluetoothGatt.writeCharacteristic(
                    writeCharacteristic, cmd, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                )
            } else {
                value = writeCharacteristic.setValue(cmd)
                bluetoothGatt.writeCharacteristic(writeCharacteristic)
            }
            Log.d(kTag, "写特征设置值结果：$value")
        }
    }

    fun disConnectDevice() {
        if (checkConnectPermission()) {
            bluetoothGatt.disconnect()
            isConnecting = false
        }
    }

    private fun checkConnectPermission(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            "缺少蓝牙连接权限".show(context)
            false
        } else {
            true
        }
    }

    private fun checkScanPermission(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            "缺少蓝牙扫描权限".show(context)
            return false
        } else {
            true
        }
    }
}