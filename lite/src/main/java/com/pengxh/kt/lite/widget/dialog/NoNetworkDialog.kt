package com.pengxh.kt.lite.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.resetParams

class NoNetworkDialog private constructor(builder: Builder) : Dialog(
    builder.context, R.style.UserDefinedDialogStyle
) {
    private val listener: OnDialogButtonClickListener = builder.listener

    class Builder {
        lateinit var context: Context
        lateinit var listener: OnDialogButtonClickListener

        fun setContext(context: Context): Builder {
            this.context = context
            return this
        }

        fun setOnDialogButtonClickListener(listener: OnDialogButtonClickListener): Builder {
            this.listener = listener
            return this
        }

        fun build(): NoNetworkDialog {
            return NoNetworkDialog(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.resetParams(Gravity.CENTER, R.style.UserDefinedAnimation, 0.85)
        setContentView(R.layout.dialog_no_network)
        setCancelable(false)
        setCanceledOnTouchOutside(false)

        val dismissView = findViewById<ImageView>(R.id.dismissView)
        dismissView.setOnClickListener { dismiss() }

        val dialogButton = findViewById<Button>(R.id.dialogButton)
        dialogButton.setOnClickListener {
            listener.onButtonClick()
            dismiss()
        }
    }

    interface OnDialogButtonClickListener {
        fun onButtonClick()
    }
}