package com.pengxh.kt.lite.utils

import com.pengxh.kt.lite.R

object Constant {
    /**
     * 广播接收者消息Key
     */
    const val BROADCAST_MESSAGE_KEY = "DataMessageKey"

    /**
     * 查看大图Intent IndexKey
     */
    const val BIG_IMAGE_INTENT_INDEX_KEY = "IndexKey"

    /**
     * 查看大图Intent Data Key
     */
    const val BIG_IMAGE_INTENT_DATA_KEY = "ImageDataKey"

    /**
     * 页面跳转Intent Data Key
     */
    const val INTENT_PARAM_KEY = "IntentParamKey"

    /**
     * 最大录音时长5分钟
     */
    const val MAX_LENGTH = 1000 * 60 * 5
    const val HTTP_TIMEOUT = 15L

    /**
     * 录音动画图标
     * */
    val AUDIO_DRAWABLES = intArrayOf(
        R.drawable.ic_audio_icon1, R.drawable.ic_audio_icon2, R.drawable.ic_audio_icon3
    )
}