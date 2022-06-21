package com.pengxh.kt.lib

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.pengxh.kt.lite.base.KotlinBaseActivity
import com.pengxh.kt.lite.utils.BroadcastManager
import com.pengxh.kt.lite.utils.Constant
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : KotlinBaseActivity() {

    private val kTag = "MainActivity"
    private lateinit var broadcastManager: BroadcastManager

    override fun initLayoutView(): Int = R.layout.activity_main

    override fun setupTopBarLayout() {

    }

    override fun initData() {
        broadcastManager = BroadcastManager.obtainInstance(this)
        broadcastManager.addAction(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.d(
                    kTag, "onReceive: " + intent?.getStringExtra(Constant.BROADCAST_INTENT_DATA_KEY)
                )
            }
        }, "TestAction")
    }

    override fun initEvent() {
        val urls = ArrayList<String>()
        urls.add("https://images.pexels.com/photos/4221068/pexels-photo-4221068.jpeg?cs=srgb&dl=pexels-clive-kim-4221068.jpg&fm=jpg")
        testButton.setOnClickListener {
            broadcastManager.sendBroadcast("TestAction", kTag)
        }
    }
}