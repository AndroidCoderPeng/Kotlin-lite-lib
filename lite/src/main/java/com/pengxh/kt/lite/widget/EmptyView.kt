package com.pengxh.kt.lite.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.pengxh.kt.lite.databinding.WidgetViewEmptyBinding

class EmptyView(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    init {
        val binding = WidgetViewEmptyBinding.inflate(LayoutInflater.from(context), this, true)
        binding.reloadButton.setOnClickListener {
            listener?.onReloadButtonClicked()
        }
    }

    private var listener: OnClickListener? = null

    fun setOnClickListener(listener: OnClickListener?) {
        this.listener = listener
    }

    interface OnClickListener {
        fun onReloadButtonClicked()
    }
}