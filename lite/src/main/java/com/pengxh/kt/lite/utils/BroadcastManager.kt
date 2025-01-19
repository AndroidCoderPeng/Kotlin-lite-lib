package com.pengxh.kt.lite.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import java.util.concurrent.ConcurrentHashMap

class BroadcastManager(private val context: Context) {

    private val receiverMap: MutableMap<String, BroadcastReceiver> = ConcurrentHashMap()
    private val intentFilter = IntentFilter()

    /**
     * 添加多个Action,广播的初始化
     */
    fun addAction(receiver: BroadcastReceiver, vararg actions: String) {
        synchronized(this) {
            for (action in actions) {
                if (!intentFilter.hasAction(action)) {
                    intentFilter.addAction(action)
                    receiverMap[action] = receiver
                }
            }
            context.registerReceiver(receiver, intentFilter)
        }
    }

    /**
     * 发送广播
     *
     * @param action 唯一码
     * @param msg    参数
     */
    fun sendBroadcast(action: String, msg: String) {
        if (action.isEmpty() || msg.isEmpty()) {
            throw IllegalArgumentException("Action and message cannot be empty")
        }
        val intent = Intent().apply {
            this.action = action
            putExtra(LiteKitConstant.BROADCAST_MESSAGE_KEY, msg)
        }
        context.sendBroadcast(intent)
    }

    /**
     * 销毁广播
     *
     * @param actions action集合
     */
    fun destroy(vararg actions: String) {
        synchronized(this) {
            for (action in actions) {
                val receiver = receiverMap.remove(action)
                if (receiver != null) {
                    try {
                        context.unregisterReceiver(receiver)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}