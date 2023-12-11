package com.pengxh.kt.lite.utils

import com.pengxh.kt.lite.R

object Constant {
    /**
     * 广播接收者消息Key
     */
    const val BROADCAST_INTENT_DATA_KEY = "DataMessageKey"

    /**
     * 查看大图Intent IndexKey
     */
    const val BIG_IMAGE_INTENT_INDEX_KEY = "IndexKey"

    /**
     * 查看大图Intent Data Key
     */
    const val BIG_IMAGE_INTENT_DATA_KEY = "ImageData"

    /**
     * 页面跳转Intent Data Key
     */
    const val INTENT_PARAM = "intentParam"

    /**
     * 最大录音时长5分钟
     */
    const val MAX_LENGTH = 1000 * 60 * 5
    const val HTTP_TIMEOUT: Long = 15

    /**
     * 连接超时时间10s
     */
    const val MAX_CONNECT_TIME = 10000L
    const val BLUETOOTH_STATE_CHANGED = "android.bluetooth.adapter.action.STATE_CHANGED"
    const val BLUETOOTH_ON = 20
    const val BLUETOOTH_OFF = 21
    const val CONNECT_SUCCESS = 22
    const val CONNECT_FAILURE = 23
    const val DISCONNECT_SUCCESS = 24
    const val SEND_SUCCESS = 25
    const val SEND_FAILURE = 26
    const val RECEIVE_SUCCESS = 27
    const val RECEIVE_FAILURE = 28
    const val DISCOVERY_DEVICE = 29
    const val DISCOVERY_OUT_TIME = 30

    /**
     * 录音动画图标
     * */
    val AUDIO_DRAWABLES = intArrayOf(
        R.drawable.ic_audio_icon1, R.drawable.ic_audio_icon2, R.drawable.ic_audio_icon3
    )
}