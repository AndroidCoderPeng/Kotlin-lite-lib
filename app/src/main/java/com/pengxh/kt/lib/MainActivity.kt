package com.pengxh.kt.lib

import android.os.Bundle
import com.pengxh.kt.lib.databinding.ActivityMainBinding
import com.pengxh.kt.lite.base.KotlinBaseActivity

class MainActivity : KotlinBaseActivity<ActivityMainBinding>() {

    private val kTag = "MainActivity"
    private val context = this@MainActivity

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }


    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}