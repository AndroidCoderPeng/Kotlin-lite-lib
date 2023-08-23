package com.pengxh.kt.lib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pengxh.kt.lib.databinding.ActivityMainBinding
import com.pengxh.kt.lite.extensions.binding
import com.pengxh.kt.lite.widget.dialog.AlertControlDialog

class MainActivity : AppCompatActivity() {

    private val kTag = "MainActivity"
    private val context = this@MainActivity
    private val binding: ActivityMainBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.showDialogButton.setOnClickListener {
            AlertControlDialog.Builder()
                .setContext(this)
                .setTitle("111")
                .setMessage("1111")
                .setNegativeButton("111")
                .setPositiveButton("1111")
                .setOnDialogButtonClickListener(object :
                    AlertControlDialog.OnDialogButtonClickListener {
                    override fun onConfirmClick() {

                    }

                    override fun onCancelClick() {

                    }
                })
                .build().show()
        }
    }
}