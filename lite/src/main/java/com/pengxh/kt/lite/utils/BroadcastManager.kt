package com.pengxh.kt.lite.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.pengxh.kt.lite.base.BaseSingleton

class BroadcastManager private constructor(private val context: Context) {

    private val kTag = "BroadcastManager"
    private var receiverMap: MutableMap<String, BroadcastReceiver> = HashMap()

    companion object : BaseSingleton<Context, BroadcastManager>() {
        override val creator: (Context) -> BroadcastManager
            get() = ::BroadcastManager
    }

    /**
     * 添加单个Action,广播的初始化
     */
    fun addAction(receiver: BroadcastReceiver?, action: String?) {
        try {
            val filter = IntentFilter()
            filter.addAction(action)
            context.registerReceiver(receiver, filter)
            receiverMap[action!!] = receiver!!
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 添加多个Action,广播的初始化
     */
    fun addAction(receiver: BroadcastReceiver?, vararg actions: String?) {
        try {
            val filter = IntentFilter()
            for (action in actions) {
                filter.addAction(action)
                receiverMap[action!!] = receiver!!
            }
            context.registerReceiver(receiver, filter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 发送广播
     *
     * @param action 唯一码
     * @param msg    参数
     */
    fun sendBroadcast(action: String?, msg: String) {
        try {
            val intent = Intent()
            intent.action = action
            intent.putExtra(Constant.BROADCAST_INTENT_DATA_KEY, msg)
            Log.d(kTag, ">>>>> $msg")
            context.sendBroadcast(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 销毁广播
     *
     * @param actions action集合
     */
    fun destroy(vararg actions: String?) {
        try {
            for (action in actions) {
                val receiver = receiverMap[action]
                if (receiver != null) {
                    context.unregisterReceiver(receiver)
                }
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }
}