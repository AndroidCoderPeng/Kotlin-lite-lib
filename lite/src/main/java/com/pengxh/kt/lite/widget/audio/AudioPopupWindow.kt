package com.pengxh.kt.lite.widget.audio

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.base.BaseSingleton
import com.pengxh.kt.lite.extensions.getScreenHeight
import com.pengxh.kt.lite.extensions.getScreenWidth

/**
 * 录音弹框，Example:
 *
 *         AudioPopupWindow.get(this).create(object : AudioPopupWindow.IAudioPopupCallback {
 *             override fun onViewCreated(window: PopupWindow, imageView: ImageView, textView: TextView) {
 *                 binding.audioButton.setOnTouchListener { v, event ->
 *                     when (event.action) {
 *                         MotionEvent.ACTION_DOWN -> {
 *                             window.showAtLocation(binding.rootView, Gravity.CENTER, 0, 0)
 *                             // 开始录音
 *                         }
 * 
 *                         MotionEvent.ACTION_UP -> {
 *                             // 结束录音并保存为文件
 *                             window.dismiss()
 *                         }
 *                     }
 *                     true
 *                 }
 *             }
 *         })
 * */
class AudioPopupWindow private constructor(private val context: Context) {

    companion object : BaseSingleton<Context, AudioPopupWindow>() {
        override val creator: (Context) -> AudioPopupWindow
            get() = ::AudioPopupWindow
    }

    fun create(callback: IAudioPopupCallback) {
        val view = View.inflate(context, R.layout.popu_microphone, null)
        val popWidth = (context.getScreenWidth() * 0.30).toInt()
        val popHeight = (context.getScreenHeight() * 0.15).toInt()
        val window = PopupWindow(view, popWidth, popHeight, true)
        window.animationStyle = R.style.PopupAnimation
        val recodeImageView = view.findViewById<ImageView>(R.id.recodeImageView)
        val recodeTextView: TextView = view.findViewById(R.id.recodeTextView)
        callback.onViewCreated(window, recodeImageView, recodeTextView)
    }

    interface IAudioPopupCallback {
        fun onViewCreated(window: PopupWindow, imageView: ImageView, textView: TextView)
    }
}