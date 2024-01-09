package com.pengxh.kt.lite.widget.audio

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.getScreenHeight
import com.pengxh.kt.lite.extensions.getScreenWidth

/**
 * 录音弹框
 * */
class AudioPopupWindow private constructor(builder: Builder) {

    private val context = builder.context
    private val callback = builder.callback

    class Builder {
        lateinit var context: Context
        lateinit var callback: OnAudioPopupCallback

        fun setContext(context: Context): Builder {
            this.context = context
            return this
        }

        fun setOnAudioPopupCallback(callback: OnAudioPopupCallback): Builder {
            this.callback = callback
            return this
        }

        fun build(): AudioPopupWindow {
            return AudioPopupWindow(this)
        }
    }


    fun create() {
        val view = View.inflate(context, R.layout.popu_microphone, null)
        val popWidth = (context.getScreenWidth() * 0.30).toInt()
        val popHeight = (context.getScreenHeight() * 0.15).toInt()
        val window = PopupWindow(view, popWidth, popHeight, true)
        window.animationStyle = R.style.PopupAnimation
        val recodeImageView = view.findViewById<ImageView>(R.id.recodeImageView)
        val recodeTextView = view.findViewById<TextView>(R.id.recodeTextView)
        callback.onViewCreated(window, recodeImageView, recodeTextView)
    }

    interface OnAudioPopupCallback {
        fun onViewCreated(window: PopupWindow, imageView: ImageView, textView: TextView)
    }
}