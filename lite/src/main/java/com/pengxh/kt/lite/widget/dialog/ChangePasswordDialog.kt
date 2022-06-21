package com.pengxh.kt.lite.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.initDialogLayoutParams
import com.pengxh.kt.lite.extensions.isLetterAndDigit
import com.pengxh.kt.lite.extensions.show

class ChangePasswordDialog private constructor(builder: Builder) : Dialog(
    builder.context, R.style.UserDefinedDialogStyle
) {
    private val ctx: Context = builder.context
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

        fun build(): ChangePasswordDialog {
            return ChangePasswordDialog(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.initDialogLayoutParams(0.85f)
        setContentView(R.layout.dialog_change_pwd)
        setCancelable(true)
        setCanceledOnTouchOutside(true)

        val oldPwdView = findViewById<TextView>(R.id.oldPwdView)
        val newPwdView = findViewById<TextView>(R.id.newPwdView)
        val confirmPwdView = findViewById<TextView>(R.id.confirmPwdView)
        val confirmButton = findViewById<Button>(R.id.confirmButton)

        confirmButton.setOnClickListener(View.OnClickListener {
            val oldPwd = oldPwdView.text.toString()
            val newPwd = newPwdView.text.toString()
            val confirmPwd = confirmPwdView.text.toString()
            if (TextUtils.isEmpty(oldPwd)) {
                "请输入原密码".show(ctx)
                return@OnClickListener
            }
            if (TextUtils.isEmpty(newPwd)) {
                "请输入新密码".show(ctx)
                return@OnClickListener
            }
            if (TextUtils.isEmpty(confirmPwd)) {
                "请再次确认密码".show(ctx)
                return@OnClickListener
            }
            if (!newPwd.isLetterAndDigit()) {
                "新密码需包含数字和字母".show(ctx)
                return@OnClickListener
            }
            if (newPwd != confirmPwd) {
                "新密码和确认密码不一致，请检查".show(ctx)
                return@OnClickListener
            }
            listener.onConfirmClick(oldPwd, newPwd)
        })
    }

    interface OnDialogButtonClickListener {
        fun onConfirmClick(oldPwd: String?, newPwd: String?)
    }
}