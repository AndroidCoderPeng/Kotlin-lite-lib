package com.pengxh.kt.lib

import com.pengxh.kt.lite.base.KotlinBaseActivity
import com.pengxh.kt.lite.widget.dialog.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : KotlinBaseActivity() {

    private val kTag = "MainActivity"

    override fun initLayoutView(): Int = R.layout.activity_main

    override fun setupTopBarLayout() {

    }

    override fun initData() {

    }

    override fun initEvent() {
        //OK
        testButton1.setOnClickListener {
            AlertControlDialog.Builder()
                .setContext(this)
                .setTitle("")
                .setMessage("")
                .setNegativeButton("")
                .setPositiveButton("")
                .setOnDialogButtonClickListener(object :
                    AlertControlDialog.OnDialogButtonClickListener {
                    override fun onConfirmClick() {

                    }

                    override fun onCancelClick() {

                    }
                }).build().show()
        }

        //OK
        testButton2.setOnClickListener {
            ChangePasswordDialog.Builder()
                .setContext(this)
                .setOnDialogButtonClickListener(object :
                    ChangePasswordDialog.OnDialogButtonClickListener {
                    override fun onConfirmClick(oldPwd: String, newPwd: String) {

                    }
                }).build().show()
        }

        //OK
        testButton3.setOnClickListener {
            AlertInputDialog.Builder()
                .setContext(this)
                .setTitle("")
                .setHintMessage("192.168.10.15")
                .setNegativeButton("")
                .setPositiveButton("")
                .setOnDialogButtonClickListener(object :
                    AlertInputDialog.OnDialogButtonClickListener {
                    override fun onConfirmClick(value: String) {

                    }

                    override fun onCancelClick() {

                    }
                }).build().show()
        }

        testButton4.setOnClickListener {
            AlertMessageDialog.Builder()
                .setContext(this)
                .setTitle("")
                .setMessage("")
                .setPositiveButton("")
                .setOnDialogButtonClickListener(object :
                    AlertMessageDialog.OnDialogButtonClickListener {
                    override fun onConfirmClick() {

                    }
                }).build().show()
        }

        //OK
        testButton5.setOnClickListener {
            NoNetworkDialog.Builder()
                .setContext(this)
                .setOnDialogButtonClickListener(object :
                    NoNetworkDialog.OnDialogButtonClickListener {
                    override fun onButtonClick() {

                    }
                }).build().show()
        }
    }
}