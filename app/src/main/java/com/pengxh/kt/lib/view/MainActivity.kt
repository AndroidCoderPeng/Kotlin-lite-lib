package com.pengxh.kt.lib.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.adapter.ViewPagerAdapter
import com.pengxh.kt.lib.databinding.ActivityMainBinding
import com.pengxh.kt.lib.fragments.AdapterPackageFragment
import com.pengxh.kt.lite.base.KotlinBaseActivity
import com.pengxh.kt.lite.extensions.convertColor
import com.pengxh.kt.lite.extensions.getStatusBarHeight

class MainActivity : KotlinBaseActivity<ActivityMainBinding>() {

    private val itemTitles = arrayOf(
        "adapter", "annotations", "base", "callback",
        "divider", "extensions", "utils", "vm",
        "widget"
    )
    private var fragmentPages: ArrayList<Fragment> = ArrayList()

    init {
        fragmentPages.add(AdapterPackageFragment())
    }

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
        binding.viewPager.adapter = ViewPagerAdapter(
            supportFragmentManager, fragmentPages, itemTitles
        )
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}