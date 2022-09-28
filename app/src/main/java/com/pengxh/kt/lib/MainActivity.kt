package com.pengxh.kt.lib

import android.graphics.Color
import com.pengxh.kt.lite.base.KotlinBaseActivity
import com.pengxh.kt.lite.widget.dialog.BottomActionSheet
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : KotlinBaseActivity() {

    private val kTag = "MainActivity"

    override fun initLayoutView(): Int = R.layout.activity_main

    override fun setupTopBarLayout() {

    }

    override fun observeRequestState() {

    }

    override fun initData() {

    }

    override fun initEvent() {
        clickButton.setOnClickListener {
            BottomActionSheet.Builder()
                .setContext(this)
                .setActionItemTitle(
                    arrayListOf(
                        "1",
                        "1",
                        "1",
                        "1",
                        "1",
                        "1"
                    )
                )
                .setItemTextColor(Color.BLUE)
                .setOnActionSheetListener(object : BottomActionSheet.OnActionSheetListener {
                    override fun onActionItemClick(position: Int) {

                    }
                }).build().show()
        }
    }
}