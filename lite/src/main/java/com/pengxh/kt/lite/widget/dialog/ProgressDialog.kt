package com.pengxh.kt.lite.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.databinding.DialogProgressBinding
import com.pengxh.kt.lite.extensions.binding
import com.pengxh.kt.lite.extensions.initDialogLayoutParams

class ProgressDialog(context: Context) : Dialog(context, R.style.UserDefinedDialogStyle) {

    private val binding: DialogProgressBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.initDialogLayoutParams(0.5f)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        binding.progressBar.progress = 0
        binding.progressText.text = "0 %"
    }

    fun setMaxProgress(maxProgress: Long) {
        binding.progressBar.max = maxProgress.toInt()
    }

    private fun getMaxProgress() = binding.progressBar.max

    fun updateProgress(progress: Long) {
        binding.progressBar.progress = progress.toInt()

        val percent = (progress.toFloat() / getMaxProgress()) * 100
        binding.progressText.text = String.format("%.2f %%", percent)
    }
}