package com.pengxh.kt.lib

import com.pengxh.kt.lite.base.KotlinBaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : KotlinBaseActivity() {

    private val kTag = "MainActivity"

    override fun initLayoutView(): Int = R.layout.activity_main

    override fun setupTopBarLayout() {

    }

    override fun initData() {

    }

    override fun initEvent() {
        testButton.setOnClickListener {

        }
    }
}