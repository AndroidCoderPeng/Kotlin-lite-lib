package com.pengxh.kt.lite.widget.audio

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.obtainScreenWidth

object AudioPopupWindow {
    fun create(context: Context, listener: IWindowListener) {
        val view = View.inflate(context, R.layout.popu_microphone, null)
        val popWidth = (context.obtainScreenWidth() * 0.35).toInt()
        val popHeight = (context.obtainScreenWidth() * 0.30).toInt()
        val window = PopupWindow(view, popWidth, popHeight, true)
        window.animationStyle = R.style.PopupAnimation
        val recodeImageView = view.findViewById<ImageView>(R.id.recodeImageView)
        val recodeTextView: TextView = view.findViewById(R.id.recodeTextView)
        listener.onViewCreated(window, recodeImageView, recodeTextView)
    }

    interface IWindowListener {
        fun onViewCreated(window: PopupWindow?, imageView: ImageView?, textView: TextView?)
    }
}