package com.pengxh.kt.lite.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.initDialogLayoutParams
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.widget.DeleteEditText

/**
 * 输入对话框
 */
class AlertInputDialog private constructor(builder: Builder) : Dialog(
    builder.context, R.style.UserDefinedDialogStyle
) {
    private val ctx = builder.context
    private val title = builder.title
    private val hint = builder.hint
    private val positiveBtn = builder.positiveBtn
    private val negativeBtn = builder.negativeBtn
    private val listener = builder.listener

    class Builder {
        lateinit var context: Context
        lateinit var title: String
        lateinit var hint: String
        lateinit var positiveBtn: String
        lateinit var negativeBtn: String
        lateinit var listener: OnDialogButtonClickListener

        fun setContext(context: Context): Builder {
            this.context = context
            return this
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setHintMessage(hint: String): Builder {
            this.hint = hint
            return this
        }

        fun setPositiveButton(name: String): Builder {
            positiveBtn = name
            return this
        }

        fun setNegativeButton(name: String): Builder {
            negativeBtn = name
            return this
        }

        fun setOnDialogButtonClickListener(listener: OnDialogButtonClickListener): Builder {
            this.listener = listener
            return this
        }

        fun build(): AlertInputDialog {
            return AlertInputDialog(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.initDialogLayoutParams(0.8f)
        setContentView(R.layout.dialog_input)
        setCanceledOnTouchOutside(false)
        initView()
    }

    private fun initView() {
        val dialogTitleView: TextView = findViewById(R.id.dialogTitleView)
        val dialogInputView: DeleteEditText = findViewById(R.id.dialogInputView)
        val dialogCancelButton = findViewById<Button>(R.id.dialogCancelButton)
        val dialogConfirmButton = findViewById<Button>(R.id.dialogConfirmButton)
        if (title.isNotBlank()) {
            dialogTitleView.text = title
        }
        if (hint.isNotBlank()) {
            dialogInputView.hint = hint
        }
        if (negativeBtn.isNotBlank()) {
            dialogCancelButton.text = negativeBtn
        }
        dialogCancelButton.setOnClickListener {
            listener.onCancelClick()
            dismiss()
        }
        if (positiveBtn.isNotBlank()) {
            dialogConfirmButton.text = positiveBtn
        }
        dialogConfirmButton.setOnClickListener(View.OnClickListener {
            val inputValue: String = dialogInputView.text.toString().trim()
            if (inputValue.isBlank()) {
                "输入错误，请检查！".show(ctx)
                return@OnClickListener
            }
            listener.onConfirmClick(inputValue)
            dismiss()
        })
    }

    interface OnDialogButtonClickListener {
        fun onConfirmClick(value: String)

        fun onCancelClick()
    }
}