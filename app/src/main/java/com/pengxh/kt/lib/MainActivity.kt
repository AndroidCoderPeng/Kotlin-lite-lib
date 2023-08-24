package com.pengxh.kt.lib

import android.os.Bundle
import com.pengxh.kt.lib.databinding.ActivityMainBinding
import com.pengxh.kt.lite.base.KotlinBaseActivity
import com.pengxh.kt.lite.widget.dialog.AlertControlDialog

class MainActivity : KotlinBaseActivity<ActivityMainBinding>() {

    private val kTag = "MainActivity"
    private val context = this@MainActivity

    override fun initViewBinding() = ActivityMainBinding.inflate(layoutInflater)


    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
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