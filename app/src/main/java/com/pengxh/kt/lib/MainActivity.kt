package com.pengxh.kt.lib

import android.os.Bundle
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.kt.lib.databinding.ActivityMainBinding
import com.pengxh.kt.lite.base.KotlinBaseActivity
import com.pengxh.kt.lite.extensions.convertColor
import com.pengxh.kt.lite.extensions.getStatusBarHeight

class MainActivity : KotlinBaseActivity<ActivityMainBinding>() {

    private val itemNames = listOf(
        "adapter", "annotations", "base", "callback",
        "divider", "extensions", "utils", "vm",
        "widget"
    )

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun setupTopBarLayout() {
        ImmersionBar.with(this)
            .statusBarDarkFont(false)
            .statusBarColorInt(R.color.mainColor.convertColor(this))
            .init()
        binding.rootView.setPadding(0, getStatusBarHeight(), 0, 0)
        binding.rootView.requestLayout()
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}