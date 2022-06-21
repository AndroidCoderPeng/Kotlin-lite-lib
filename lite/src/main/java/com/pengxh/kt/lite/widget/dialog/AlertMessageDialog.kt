package com.pengxh.kt.lite.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.initDialogLayoutParams

/**
 * 普通提示对话框对话框
 */
class AlertMessageDialog private constructor(builder: Builder) : Dialog(
    builder.context, R.style.UserDefinedDialogStyle
) {
    private val title = builder.title
    private val message = builder.message
    private val positiveBtn = builder.positiveBtn
    private val listener = builder.listener

    class Builder {
        lateinit var context: Context
        lateinit var title: String
        lateinit var message: String
        lateinit var positiveBtn: String
        lateinit var listener: OnDialogButtonClickListener

        fun setContext(context: Context): Builder {
            this.context = context
            return this
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun setPositiveButton(name: String): Builder {
            this.positiveBtn = name
            return this
        }

        fun setOnDialogButtonClickListener(listener: OnDialogButtonClickListener): Builder {
            this.listener = listener
            return this
        }

        fun build(): AlertMessageDialog {
            return AlertMessageDialog(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.initDialogLayoutParams(0.8f)
        setContentView(R.layout.dialog_message)
        setCanceledOnTouchOutside(false)
        initView()
    }

    private fun initView() {
        val dialogTitleView: TextView = findViewById(R.id.dialogTitleView)
        val dialogMessageView: TextView = findViewById(R.id.dialogMessageView)
        val dialogConfirmButton = findViewById<Button>(R.id.dialogConfirmButton)
        if (title.isNotBlank()) {
            dialogTitleView.text = title
        }
        if (message.isNotBlank()) {
            dialogMessageView.text = message
        }
        if (!TextUtils.isEmpty(positiveBtn)) {
            dialogConfirmButton.text = positiveBtn
        }
        dialogConfirmButton.setOnClickListener {
            listener.onConfirmClick()
            dismiss()
        }
    }

    interface OnDialogButtonClickListener {
        fun onConfirmClick()
    }
}