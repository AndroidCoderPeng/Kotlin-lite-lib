package com.pengxh.kt.lite.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class BroadcastManager(private val context: Context) {

    private var receiverMap: MutableMap<String, BroadcastReceiver> = HashMap()

    /**
     * 添加单个Action,广播的初始化
     */
    fun addAction(receiver: BroadcastReceiver, action: String) {
        val filter = IntentFilter()
        filter.addAction(action)
        context.registerReceiver(receiver, filter)
        receiverMap[action] = receiver
    }

    /**
     * 添加多个Action,广播的初始化
     */
    fun addAction(receiver: BroadcastReceiver, vararg actions: String) {
        val filter = IntentFilter()
        for (action in actions) {
            filter.addAction(action)
            receiverMap[action] = receiver
        }
        context.registerReceiver(receiver, filter)
    }

    /**
     * 发送广播
     *
     * @param action 唯一码
     * @param msg    参数
     */
    fun sendBroadcast(action: String, msg: String) {
        val intent = Intent()
        intent.action = action
        intent.putExtra(LiteKitConstant.BROADCAST_MESSAGE_KEY, msg)
        context.sendBroadcast(intent)
    }

    /**
     * 销毁广播
     *
     * @param actions action集合
     */
    fun destroy(vararg actions: String) {
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