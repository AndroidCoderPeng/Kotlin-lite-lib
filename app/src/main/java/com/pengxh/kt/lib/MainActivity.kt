package com.pengxh.kt.lib

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pengxh.kt.lite.activity.BigImageActivity
import com.pengxh.kt.lite.base.KotlinBaseActivity
import com.pengxh.kt.lite.extensions.navigatePageTo
import com.pengxh.kt.lite.utils.BroadcastManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : KotlinBaseActivity() {

    private val kTag = "MainActivity"
    private lateinit var broadcastManager: BroadcastManager

    override fun initLayoutView(): Int = R.layout.activity_main

    override fun setupTopBarLayout() {

    }

    override fun initData() {
        val urls = ArrayList<String>()
        urls.add("https://images.pexels.com/photos/4221068/pexels-photo-4221068.jpeg?cs=srgb&dl=pexels-clive-kim-4221068.jpg&fm=jpg")

        broadcastManager = BroadcastManager.obtainInstance(this)
        broadcastManager.addAction(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                context?.navigatePageTo<BigImageActivity>(0, urls)
            }
        }, "TestAction")
    }

    override fun initEvent() {
        testButton.setOnClickListener {
            broadcastManager.sendBroadcast("TestAction", kTag)
        }
    }
}