package com.pengxh.kt.lite.widget

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.pengxh.kt.lite.R

class KeyBoardView constructor(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs), View.OnClickListener {

    private var listener: KeyboardClickListener? = null

    init {
        View.inflate(context, R.layout.widget_view_keyboard, this)
        setChildViewOnclick(this)
    }

    /**
     * 设置键盘子View的点击事件
     */
    private fun setChildViewOnclick(parent: ViewGroup) {
        val childCount: Int = parent.childCount
        for (i in 0 until childCount) {
            val view: View = parent.getChildAt(i)
            if (view is ViewGroup) {
                setChildViewOnclick(view)
                continue
            }
            view.setOnClickListener(this)
        }
    }

    fun dispatchKeyEventInFullScreen(event: KeyEvent?): Boolean {
        if (event == null) {
            return false
        }
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShown) {
                this.visibility = View.GONE
                return true
            }
        }
        return false
    }

    override fun onClick(v: View) {
        if (v is TextView) {
            // 如果点击的是TextView
            val value = v.text.toString()
            if (value.isNotBlank()) {
                listener?.onClick(value)
            }
        } else if (v is ImageView) {
            // 如果是图片那肯定点击的是删除
            listener?.onDelete()
        }
    }

    fun setKeyboardClickListener(keyboardClickListener: KeyboardClickListener?) {
        listener = keyboardClickListener
    }

    interface KeyboardClickListener {
        fun onClick(value: String)

        fun onDelete()
    }
}