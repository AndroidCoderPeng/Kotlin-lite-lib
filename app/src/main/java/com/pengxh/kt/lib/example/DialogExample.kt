package com.pengxh.kt.lib.example

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import com.pengxh.kt.lib.databinding.DialogExampleDialogExtensionBinding
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.binding
import com.pengxh.kt.lite.extensions.resetParams

class DialogExample(context: Context, private val ratio: Float, private val gravity: Int) :
    Dialog(context) {

    private val binding: DialogExampleDialogExtensionBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.resetParams(gravity, R.style.UserDefinedAnimation, ratio)

        binding.dialogConfirmButton.setTextColor(Color.RED)
    }
}